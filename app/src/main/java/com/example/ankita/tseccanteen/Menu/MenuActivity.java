package com.example.ankita.tseccanteen.Menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ankita.tseccanteen.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodsAnkita");

    String name, price, availability, description;
    EditText foodName, foodPrice, foodDescription;
    RadioGroup availableRadioGroup;
    Button addFoodItem, changeAvailability;

    RecyclerView menuRecyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<MenuModalClass> menuList = new ArrayList<>();
    MenuAdapter.OnClickListener onClickListener;
    FloatingActionButton addMenuItem;
    Context context;
    int totalFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = MenuActivity.this;
        menuRecyclerView = findViewById(R.id.rv_menu);
        addMenuItem = findViewById(R.id.fab_add_menu_item);

        addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                                                .customView(R.layout.add_menu_item_layout, false)
                                                .title("Add Food Item")
                                                .show();

                foodName = (EditText) materialDialog.findViewById(R.id.et_food_name);
                foodPrice = (EditText) materialDialog.findViewById(R.id.et_food_price);
                foodDescription = (EditText) materialDialog.findViewById(R.id.et_food_description);
                availableRadioGroup = (RadioGroup) materialDialog.findViewById(R.id.rg_availability);
                addFoodItem = (Button) materialDialog.findViewById(R.id.btn_add_menu_item);

                availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_yes:
                                availability = "Yes";
                                break;

                            case R.id.rb_no:
                                availability = "No";
                                break;
                        }
                    }
                });

                addFoodItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name = foodName.getText().toString();
                        price = foodPrice.getText().toString();
                        description = foodDescription.getText().toString();
                        int foodId = totalFood + 1;
                        FoodItem foodItem = new FoodItem(name, price, availability, description);
                        databaseReference.child("F"+foodId).setValue(foodItem);
                        materialDialog.dismiss();
                    }
                });
            }

        });


        onClickListener = new MenuAdapter.OnClickListener() {
            @Override
            public void onMenuItemClick(final int position) {
                final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                        .customView(R.layout.add_menu_item_layout, false)
                        .title("Availability")
                        .show();

                foodName = (EditText) materialDialog.findViewById(R.id.et_food_name);
                foodPrice = (EditText) materialDialog.findViewById(R.id.et_food_price);
                foodDescription = (EditText) materialDialog.findViewById(R.id.et_food_description);
                availableRadioGroup = (RadioGroup) materialDialog.findViewById(R.id.rg_availability);
                addFoodItem = (Button) materialDialog.findViewById(R.id.btn_add_menu_item);
                changeAvailability = (Button) materialDialog.findViewById(R.id.btn_change_available);

                foodName.setVisibility(View.GONE);
                foodPrice.setVisibility(View.GONE);
                foodDescription.setVisibility(View.GONE);
                addFoodItem.setVisibility(View.GONE);
                changeAvailability.setVisibility(View.VISIBLE);

                availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_yes:
                                availability = "Yes";
                                break;

                            case R.id.rb_no:
                                availability = "No";
                                break;
                        }
                    }
                });

                changeAvailability.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String foodId = "F"+(position+1);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodsAnkita")
                                                                .child(foodId).child("availability");
                        databaseReference.setValue(availability);
                        materialDialog.dismiss();
                    }
                });

            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuList.clear();
                for (DataSnapshot menuSnapshot: dataSnapshot.getChildren()) {
                    MenuModalClass menuModalClass = menuSnapshot.getValue(MenuModalClass.class);
                    menuList.add(menuModalClass);
                }

                menuRecyclerView.setHasFixedSize(true);
                menuRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                adapter = new MenuAdapter(MenuActivity.this, menuList, onClickListener);
                totalFood = menuList.size();
                menuRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
