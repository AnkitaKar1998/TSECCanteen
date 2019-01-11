package com.example.ankita.tseccanteen.Orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ankita.tseccanteen.MainActivity;
import com.example.ankita.tseccanteen.Menu.MenuActivity;
import com.example.ankita.tseccanteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    Context context;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("R_Orders");

    ArrayList<OrdersModalClass> ordersList = new ArrayList<>();
    RecyclerView orderRecyclerView;
    RecyclerView.Adapter adapter;
    OrdersAdapter.OnOrderClickListener onOrderClickListener;

    EditText otp;
    Button verifyOtp;

    String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        context = OrdersActivity.this;
        firebaseAuth = FirebaseAuth.getInstance();

        orderRecyclerView = findViewById(R.id.rv_orders);
        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));

        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("currentUser");

        onOrderClickListener = new OrdersAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(final String orderNo, final CardView cardView, final ImageView imageView) {
                final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                                                .title("Verify OTP")
                                                .customView(R.layout.dialog_verify_order, false)
                                                .show();

                otp = (EditText) materialDialog.findViewById(R.id.et_otp);
                verifyOtp = (Button) materialDialog.findViewById(R.id.btn_verify);

                verifyOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final String inputOtp = otp.getText().toString();

                        Query query = FirebaseDatabase.getInstance().getReference().child("R_Orders").orderByChild("order_id").equalTo(orderNo);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data : dataSnapshot.getChildren()) {
                                    String otp = data.child("otp").getValue(String.class);
                                    if(inputOtp.equals(otp)) {
                                        imageView.setVisibility(View.VISIBLE);
                                        cardView.setCardBackgroundColor(Color.parseColor("#E4FFDC"));

                                    } else {
                                        Toast.makeText(context, "Wrong OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        materialDialog.dismiss();
                    }
                });

            }
        };

    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.item_menu) {
            Intent intent = new Intent(OrdersActivity.this, MenuActivity.class);
            startActivity(intent);

        } else if (menuItem.getItemId() == R.id.item_logout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                ordersList.clear();
                for(DataSnapshot orderSnapShot: dataSnapshot.getChildren()) {
                    OrdersModalClass ordersModalClass = orderSnapShot.getValue(OrdersModalClass.class);
                    ordersList.add(ordersModalClass);
                }
                orderRecyclerView = findViewById(R.id.rv_orders);
                adapter = new OrdersAdapter(OrdersActivity.this, ordersList, onOrderClickListener);
                orderRecyclerView.setHasFixedSize(true);
                orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
                orderRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
