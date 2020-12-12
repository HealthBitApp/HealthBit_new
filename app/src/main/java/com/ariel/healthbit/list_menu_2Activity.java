package com.ariel.healthbit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class list_menu_2Activity extends AppCompatActivity {

    public static String signin_uid = null;

    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference refEvent;
    FirebaseUser user;

    EditText edit_product;
    EditText edit_calories;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu_2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTIPS);
        setSupportActionBar(toolbar);
        Button back = (Button) findViewById(R.id.backTips);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        // Check is status of login and getting the uid
        user = FirebaseAuth.getInstance().getCurrentUser();
        signin_uid = user.getUid();

        // Setting up the elements
        edit_product = (EditText) findViewById(R.id.edit_product);
        edit_calories = (EditText) findViewById(R.id.edit_calories);
        btn_save = (Button) findViewById(R.id.btn_save);

        // Insert into product name the search_k value
        edit_product.setText(list_menu_activity.search_query);

        // If Save Button click
        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                ref = database.getReference("ProductsUsers/"+ signin_uid+"/");
                refEvent = database.getReference("ProductsEvents/"+ signin_uid+"/");
                Map<String, Object> products = new HashMap<>();
                Map<String, Object> events = new HashMap<>();

                String tmp = edit_product.getText().toString();
                String tmp2 = edit_calories.getText().toString();

                Toast.makeText(view.getContext(), "T="+tmp+"b", Toast.LENGTH_SHORT).show();
                // Creating ProductEvent in db
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                ProductEvent pe = new ProductEvent(dailymenu.type, 1, Integer.parseInt(tmp2),  tmp, simpleDateFormat.format(new Date()));
                events.put(Long.toHexString(Double.doubleToLongBits(Math.random())), pe);
                refEvent.updateChildren(events);
                //refEvent.setValue(events);

                Product p = new Product(Integer.parseInt(tmp2),  tmp);
                products.put(Long.toHexString(Double.doubleToLongBits(Math.random())), p);
                ref.updateChildren(products);
                //list_menu_activity.list.notify();
                //list_menu_activity.list.add(pe);
                //list_menu_activity.set_new_row(pe);
                list_menu_activity.adapter.notifyDataSetChanged();
                //list_menu_activity.clear_adapter();
                //list_menu_activity.set_up_user_rows();
                //list_menu_activity.set_new_row(p.getFullName());
                //list_menu_activity.set_adapter();

                //ListMenuActivity.myList.add(p.getFullName());
                //ListMenuActivity.adapter = new ArrayAdapter(list_menu_2Activity.this, android.R.layout.simple_list_item_1, android.R.id.text1, ListMenuActivity.myList);
                //ListMenuActivity.listView.setAdapter(ListMenuActivity.adapter);

                //ref.setValue(users2);

                Toast.makeText(list_menu_2Activity.this, "We save it:)", Toast.LENGTH_LONG).show();
                finish();

                //"{'name':'demo','kal':1}"


            }
        });

        //Toast.makeText(list_menu_2Activity.this, "Title="+ ListMenuActivity.Search_k, Toast.LENGTH_LONG).show();
    }
}