package com.example.ankita.tseccanteen.Orders;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("OrdersAnkita");
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users");

    ArrayList<OrdersModalClass> ordersList = new ArrayList<>();
    RecyclerView orderRecyclerView;
    RecyclerView.Adapter adapter;

    String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        firebaseAuth = FirebaseAuth.getInstance();


        orderRecyclerView = findViewById(R.id.rv_orders);
        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));

        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("currentUser");

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
        Log.d("krishna", "Next");

        /* Rahuls side - Dtudent order retrival*/

//        Query query = FirebaseDatabase.getInstance().getReference().child("OrdersAnkita").orderByChild("studentId").equalTo(currentUserId);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    Log.d("krishna", "ankita before");
//                    OrdersModalClass ordersModalClass = snapshot.getValue(OrdersModalClass.class);
//                    Log.d("krishna", "ankita after");
//                    Log.d("krishna", "Order No: "+ordersModalClass.getOrderNo());
//                    Log.d("krishna", "Amount: "+ordersModalClass.getAmount());
//                    ordersList.add(ordersModalClass);
//                }
//
//                orderRecyclerView = findViewById(R.id.rv_orders);
//                adapter = new OrdersAdapter(OrdersActivity.this, ordersList);
//                orderRecyclerView.setHasFixedSize(true);
//                orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
//                orderRecyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        /* Canteen side - Order retrival */

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Log.d("krishna", "Hello");
                ordersList.clear();
                for(DataSnapshot orderSnapShot: dataSnapshot.getChildren()) {
                    /* Canteen Side orders retrieval */

                    Log.d("krishna", "ankita before");
                    OrdersModalClass ordersModalClass = orderSnapShot.getValue(OrdersModalClass.class);
                    Log.d("krishna", "ankita after");
                    ordersList.add(ordersModalClass);
                }
                orderRecyclerView = findViewById(R.id.rv_orders);
                adapter = new OrdersAdapter(OrdersActivity.this, ordersList);
                orderRecyclerView.setHasFixedSize(true);
                orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
                orderRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("krishna", "Hello error");
            }
        });

    }

}
