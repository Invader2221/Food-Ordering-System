package com.example.quick_food.recycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.Adapters.OrderQueueAdapter;
import com.example.quick_food.GetterSetters.OrderDetails;
import com.example.quick_food.QRCodeScanner;
import com.example.quick_food.R;
import com.example.quick_food.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import static com.example.quick_food.Login.MY_PREFS_NAME;

public class OrderQueue extends AppCompatActivity {


    Button orderHistory, ongoinOrders;
    RecyclerView mRecycleView;
    OrderDetails orderDetails;
    List<OrderDetails> orderDetailsList;
    FirebaseFirestore db;
    KProgressHUD progressHUD;
    String isVendorLogged;
    String currentUserId;
    boolean isOngingOrders;
    private boolean userIsVender = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_queue);
        getSupportActionBar().setTitle("Orders - Ongoing");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        orderHistory = (Button) findViewById(R.id.order_history);
        ongoinOrders = (Button) findViewById(R.id.ongoing_orders);

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_Order_queue);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(OrderQueue.this, 1);
        mRecycleView.setLayoutManager(gridLayoutManager);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        isVendorLogged = prefs.getString("userIsVender", "");
        currentUserId = prefs.getString("loggedUserId", "");

        progressHUD = KProgressHUD.create(OrderQueue.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        isOngingOrders = true;

        orderHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isOngingOrders = false;
                getSupportActionBar().setTitle("Orders - History");
                setDatalist();
            }
        });


        ongoinOrders.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isOngingOrders = true;
                getSupportActionBar().setTitle("Orders - Ongoing");
                setDatalist();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setDatalist();
    }

    private void setDatalist() {
        progressHUD.show();
        db = FirebaseFirestore.getInstance();
        orderDetailsList = new ArrayList<>();


        db.collection("OrderDetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Doc", document.getId() + " => " + document.getData());

                                final String status = document.getString("Status");
                                final String userId = document.getString("userId");
                                final String totalCost = document.getString("Total");
                                final Double totalItems = document.getDouble("TotalItems");

                                if (isVendorLogged.equals("")) {
                                    if (currentUserId.equals(userId)) {
                                        if (isOngingOrders) {

                                            if (status.equals("P") || status.equals("A") || status.equals("C")) {
                                                orderDetails = new OrderDetails(document.getId(), status, userId, totalCost, String.valueOf(totalItems));
                                                orderDetailsList.add(orderDetails);
                                            }

                                        } else {

                                            if (status.equals("R") || status.equals("D")) {
                                                orderDetails = new OrderDetails(document.getId(), status, userId, totalCost, String.valueOf(totalItems));
                                                orderDetailsList.add(orderDetails);
                                            }

                                        }

                                    }
                                } else {

                                    if (isOngingOrders) {
                                        if (status.equals("P") || status.equals("A") || status.equals("C")) {
                                            orderDetails = new OrderDetails(document.getId(), status, userId, totalCost, String.valueOf(totalItems));
                                            orderDetailsList.add(orderDetails);

                                        }

                                    } else {

                                        if (status.equals("R") || status.equals("D")) {
                                            orderDetails = new OrderDetails(document.getId(), status, userId, totalCost, String.valueOf(totalItems));
                                            orderDetailsList.add(orderDetails);

                                        }

                                    }

                                }

                                OrderQueueAdapter myAdapter = new OrderQueueAdapter(OrderQueue.this, orderDetailsList);
                                mRecycleView.setAdapter(myAdapter);

                                progressHUD.dismiss();
                            }


                        } else {
                            Log.d("Doc", "Error getting documents: ", task.getException());
                            progressHUD.dismiss();
                        }
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String theVender = prefs.getString("userIsVender", "");


        MenuItem QRScanner = menu.findItem(R.id.QR_scanner);
        MenuItem Profile = menu.findItem(R.id.profile_menu);
        MenuItem Cart = menu.findItem(R.id.cart_icon_menu);
        MenuItem Order = menu.findItem(R.id.Orders);


        if (!theVender.equals("")) {
            userIsVender = true;

            QRScanner.setVisible(true);
            Profile.setVisible(false);
            Cart.setVisible(false);
            Order.setVisible(false);
        } else {

            QRScanner.setVisible(false);
            Profile.setVisible(false);
            Cart.setVisible(false);
            Order.setVisible(false);

        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.QR_scanner) {
            Intent intent = new Intent(OrderQueue.this, QRCodeScanner.class);
            startActivity(intent);
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}