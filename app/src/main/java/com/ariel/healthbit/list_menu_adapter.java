package com.ariel.healthbit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_menu_adapter extends BaseAdapter implements ListAdapter {
    private ArrayList<ProductEvent> list = new ArrayList<ProductEvent>();
    private Context context;
    private int value;

    private TextView view_product;
    private EditText edit_count;
    private Button btn_less;
    private Button btn_more;

    public FirebaseDatabase database;
    public DatabaseReference ref;
    public Query event_of_today;

    public list_menu_adapter(ArrayList<ProductEvent> list, Context context) {
        this.list    = list;
        this.context = context;
        this.value   = 1;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //list.get(pos).getBytes();
        //return list.get(pos);
        //just return 0 if your list items do not have an Id variable.
    }

    public EditText get_edit_count() {
        return this.edit_count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_info, null);
        }

        Toast.makeText(context, "P="+ String.valueOf(position)+"\nN="+list.get(position).getFullName(), Toast.LENGTH_SHORT).show();

        //Handle TextView and display string from your list
        view_product = (TextView) view.findViewById(R.id.view_product);
        view_product.setText(list.get(position).getFullName());

        //Handle buttons and add onClickListeners
        edit_count = (EditText) view.findViewById(R.id.edit_count);
        edit_count.setText(String.valueOf(list.get(position).getCount()));
        btn_less = (Button)view.findViewById(R.id.btn_less);
        btn_more = (Button)view.findViewById(R.id.btn_more);
        this.value = Integer.parseInt(edit_count.getText().toString());

        btn_less.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                value = value - 1;
                if (value == 0) {

                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference("ProductsEvents/"+ MainActivity.uid);
                    event_of_today = ref.orderByChild("start").equalTo(list_menu_activity.get_today_date());
                    event_of_today.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                ProductEvent pe = ds.getValue(ProductEvent.class);
                                //Toast.makeText(v.getContext(), "f="+ pe.getFullName() + "\nf="+list.get(position), Toast.LENGTH_SHORT).show();
                                if (pe.getType() == dailymenu.type && pe.getFullName().contentEquals(list.get(position).getFullName()) ){
                                    //Toast.makeText(v.getContext(), "show="+ ds.getKey(), Toast.LENGTH_SHORT).show();
                                    ref.child(ds.getKey()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    list.remove(position);


                    notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Removed!", Toast.LENGTH_SHORT).show();
                } else {
                    get_edit_count().setText(String.valueOf(value));
                    update_db_on_count_change(position);
                    //Toast.makeText(v.getContext(), "Here1"+edit_count.getText().toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
        btn_more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //int value = Integer.parseInt();
                //edit_product.setText(value+1);
                //value = Integer.parseInt(edit_count.getText().toString());
                value = value + 1;
                edit_count.setText(String.valueOf(value));
                Toast.makeText(v.getContext(), "Here2 "+ edit_count.getText().toString(), Toast.LENGTH_LONG).show();
                update_db_on_count_change(position);
            }

        });
        return view;
    }

    public void update_db_on_count_change(int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ProductsEvents/"+ MainActivity.uid+"/");
        event_of_today = ref.orderByChild("start").equalTo(list_menu_activity.get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    //Toast.makeText(v.getContext(), "f="+ pe.getFullName() + "\nf="+list.get(position), Toast.LENGTH_SHORT).show();
                    if (pe.getType() == dailymenu.type && pe.getFullName().contentEquals(list.get(position).getFullName()) ){
                        //Toast.makeText(v.getContext(), "show="+ ds.getKey(), Toast.LENGTH_SHORT).show();
                        pe.setCount(value);
                        ref.child(ds.getKey()).setValue(pe);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
