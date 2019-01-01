package com.example.ankita.tseccanteen;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodsAnkita");

    RecyclerView menuRecyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<MenuModalClass> menuList = new ArrayList<>();
    FloatingActionButton addMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuRecyclerView = findViewById(R.id.rv_menu);
        addMenuItem = findViewById(R.id.fab_add_menu_item);

        addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d("urmi","Next");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("urmi","Next");
                for (DataSnapshot menuSnapshot: dataSnapshot.getChildren()) {
                    Log.d("urmi","before ankita");
                    MenuModalClass menuModalClass = menuSnapshot.getValue(MenuModalClass.class);
                    Log.d("urmi","after ankita");
                    Log.d("urmi",menuModalClass.getName());
                    Log.d("urmi",menuModalClass.getPrice());
                    Log.d("urmi",menuModalClass.getAvailability());
                    Log.d("urmi",menuModalClass.getDescription());
                    menuList.add(menuModalClass);
                }
                menuRecyclerView.setHasFixedSize(true);
                menuRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                adapter = new MenuAdapter(MenuActivity.this, menuList);
                menuRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
