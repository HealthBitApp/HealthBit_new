package com.ariel.healthbit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class list_menu_activity  extends AppCompatActivity {

    public static FirebaseDatabase database;
    public static DatabaseReference ref;
    public static Query event_of_today;

    public static AutoCompleteTextView search_autoComplete;
    public static ImageButton search_button;
    public static String search_query;
    public static ArrayList<String> search_list;

    public static list_menu_adapter adapter;
    public static ArrayList<ProductEvent> list;
    public static ListView listview;
    public static Button list_button;

    public static Context context;

    public static String today_date = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTIPS);
        setSupportActionBar(toolbar);
        Button back = (Button) findViewById(R.id.backTips);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });

        Toast.makeText(this, "hello1", Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("ProductsEvents/"+ MainActivity.uid);

        search_autoComplete=findViewById(R.id.autoComplete);
        search_button=(ImageButton) findViewById(R.id.btn_search);
        search_list = new ArrayList<>();

        list = new ArrayList<ProductEvent>();
        listview = (ListView) findViewById(R.id.listView); // Define the listview
        list_button=(Button) findViewById(R.id.backTips2);

        Toast.makeText(this, "hello2", Toast.LENGTH_SHORT).show();
        set_up_user_rows();
        set_adapter();
        set_list_clear_button();

        set_search_options_of_owner();
        set_search_options_of_globals();
        set_search_options_init();
        set_search_options_button();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public static String get_today_date() {
        if (today_date == null) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            today_date = simpleDateFormat.format(new Date());
        }
        return today_date;
    }

    public static void set_up_user_rows() {
        //list = new ArrayList<ProductEvent>();
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    if ( pe.getType() == dailymenu.type && list_search_by_fullname(pe.getFullName()) == false ){
                        set_new_row( pe );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void set_new_row(ProductEvent pe) {
        list.add(pe);
        adapter.notifyDataSetChanged();
    }

    public static void set_new_row_in_db(String fullname) {
        String[] argc = fullname.split(" \\(");
        argc[1] = argc[1].replace(" cal)", "");
        int cal = Integer.parseInt(argc[1].toString());
        String name = argc[0];
        Toast.makeText(context, "N="+name+"d", Toast.LENGTH_SHORT).show();
        ProductEvent pe = new ProductEvent(dailymenu.type, 1, cal, name, get_today_date());
        ref.child(Long.toHexString(Double.doubleToLongBits(Math.random()))).setValue(pe);
        list.add(pe);
        adapter.notifyDataSetChanged();
        //Toast.makeText(context, "argc0="+argc[0], Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "argc1="+argc[1], Toast.LENGTH_SHORT).show();
    }

    public static void set_adapter() {
        adapter = new list_menu_adapter(list, context);
        listview.setAdapter(adapter);
    }

    public static void clear_adapter() { list_menu_activity.list.removeAll(list); /*list = new ArrayList<ProductEvent>();*/ }

    public void set_list_clear_button() {
        list_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ( list.size() > 0 ) {
                    remove_all_from_db();
                    clear_adapter();
                    set_adapter();
                    Toast.makeText(context, "The list is cleared!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "The list is allredy empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean list_search_by_fullname(String fullname) {
        for (ProductEvent pe: list) {
            if (pe.getFullName().contentEquals(fullname)) {
                //Toast.makeText(context, "Found", Toast.LENGTH_SHORT).show();
                return true;
            }
            //Toast.makeText(context, "Pe="+pe.getFullName(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static void remove_from_db(String content) {
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);

                    if (pe.getType() == dailymenu.type && pe.getFullName() == content){
                        Log.d("value", ds.getKey()+"dddd");
                        //ref.child(ds.getKey()).setValue("demo");
                        //Toast.makeText(context, "demo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void remove_all_from_db() {
        event_of_today = ref.orderByChild("start").equalTo(get_today_date());
        event_of_today.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    ProductEvent pe = ds.getValue(ProductEvent.class);
                    ref.child(ds.getKey()).setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void set_search_options_of_owner() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    search_list.add(p.getFullName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void set_search_options_of_globals() {
        ref = database.getReference("products");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    search_list.add(p.getFullName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref = database.getReference("ProductsEvents/"+ MainActivity.uid);
    }

    public static void set_search_options_init() {

        //ArrayAdapter arrayAdapter=new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, search_list);
        ArrayAdapter arrayAdapter=new ArrayAdapter(context, R.layout.search_info, R.id.text, search_list);
        search_autoComplete.setAdapter(arrayAdapter);
        search_autoComplete.setThreshold(1);
        search_autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", String.valueOf(s));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.valueOf(s));
                list_menu_activity.search_query = String.valueOf(s);
                //Toast.makeText(context, "Hello "+ search_query, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", String.valueOf(s));
            }
        });
    }

    public void set_search_options_button() {
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (search_list.contains(search_query)) {
                    if (list_search_by_fullname(search_query)==true) {
                        Log.d("ExistValueInYourList", search_query);
                        Toast.makeText(context, "You allredy add this item.", Toast.LENGTH_LONG).show();
                        search_query = "";
                    } else {
                        Log.d("ExistValue", search_query);
                        set_new_row_in_db(search_query);
                        //list_menu_activity.clear_adapter();
                        //list_menu_activity.set_up_user_rows();
                        list_menu_activity.set_adapter();
                        search_query = "";
                    }
                } else {
                    Log.d("UnExistValue", search_query);
                    Intent myIntent = new Intent(context, list_menu_2Activity.class);
                    startActivity(myIntent);
                }
                search_autoComplete.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        search_autoComplete.showDropDown();
                    }
                },500);
                String tmp = search_query;
                search_autoComplete.setText("");
                search_autoComplete.setSelection(search_autoComplete.getText().length());
                search_query = tmp;
            }
        });
    }

}
