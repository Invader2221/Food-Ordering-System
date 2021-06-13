package com.example.quick_food.recycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.quick_food.Adapters.CartViewAdapter;
import com.example.quick_food.Firebase.MySingleton;
import com.example.quick_food.GetterSetters.CartDetails;
import com.example.quick_food.R;
import com.example.quick_food.ShareBarcode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.quick_food.Login.MY_PREFS_NAME;
import static com.example.quick_food.Utils.FCM_API;
import static com.example.quick_food.Utils.serverKey;

public class MyCartActivity extends AppCompatActivity {

    RecyclerView mRecycleView;
    CartDetails mcartData;
    FirebaseFirestore db;
    SharedPreferences prefs;
    Button placeOrderBtn;
    String contentType = "application/json";
    String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    KProgressHUD progressHUD;
    String orderStatus;
    String isVendorLogged;
    String curentOrderId;
    String currentUserId;

    public static List<CartDetails> myCartList;
    static double totalValueForAllCart;
    static TextView mTxtTotalForCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cart);
        getSupportActionBar().setTitle("My Cart");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        placeOrderBtn = findViewById(R.id.btn_placeorder);

        progressHUD = KProgressHUD.create(MyCartActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("We are generating your order.")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        db = FirebaseFirestore.getInstance();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        isVendorLogged = prefs.getString("userIsVender", "");


        mTxtTotalForCart = findViewById(R.id.tv_total);
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_cart);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyCartActivity.this, 1);
        mRecycleView.setLayoutManager(gridLayoutManager);

        if (getIntent().getStringExtra("EXTRA_ORDER_ID") != null) {
            curentOrderId = getIntent().getStringExtra("EXTRA_ORDER_ID");
            currentUserId = getIntent().getStringExtra("EXTRA_ORDER_USER_ID");
            setDatalist();

        } else {
            if (myCartList == null || myCartList.isEmpty()) {
                myCartList = new ArrayList<>();
            }

            final String foodImage = getIntent().getStringExtra("image_Name");
            final String foodName = getIntent().getStringExtra("item_name");
            final String foodPrice = getIntent().getStringExtra("total_price");
            final String foodId = getIntent().getStringExtra("item_id");
            final String addersAndSizes = getIntent().getStringExtra("selected_adders_sizes");

            if (foodId != null) {
                mcartData = new CartDetails(foodId, foodName, foodPrice, foodImage, addersAndSizes);
                myCartList.add(mcartData);
            }

            method();

            CartViewAdapter myAdapter = new CartViewAdapter(MyCartActivity.this, myCartList, true);
            mRecycleView.setAdapter(myAdapter);
        }

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (curentOrderId != null) {

                    if (isVendorLogged.equals("")) {
                        Intent intent = new Intent(MyCartActivity.this, ShareBarcode.class);
                        intent.putExtra("DECRYPTDATA", curentOrderId);
                        startActivity(intent);
                    } else {

                        if (orderStatus.equals("A")) {

                            progressHUD.show();

                            Map<String, Object> get_data = new HashMap<>();
                            get_data.put("Status", "C");


                            Log.e("DocPath >>", curentOrderId);

                            db.collection("OrderDetails").document(curentOrderId).update(get_data)

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(MyCartActivity.this, "Data successfully written! update", Toast.LENGTH_LONG).show();

                                            TOPIC = "/topics/" + currentUserId.toString();
                                            NOTIFICATION_TITLE = "Complete Order";
                                            NOTIFICATION_MESSAGE = "Your Order has been completed. Click here to see details";

                                            JSONObject notification = new JSONObject();
                                            JSONObject notifcationBody = new JSONObject();
                                            try {
                                                notifcationBody.put("title", NOTIFICATION_TITLE);
                                                notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                                notifcationBody.put("orderId", curentOrderId);

                                                notification.put("to", TOPIC);
                                                notification.put("data", notifcationBody);
                                            } catch (JSONException e) {
                                                Log.e(TAG, "onCreate: " + e.getMessage());
                                            }
                                            sendNotification(notification);


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressHUD.dismiss();
                                            Toast.makeText(MyCartActivity.this, "Data writing Error update", Toast.LENGTH_LONG).show();

                                        }
                                    });

                        } else if (orderStatus.equals("P")) {
                            progressHUD.show();

                            Map<String, Object> get_data = new HashMap<>();
                            get_data.put("Status", "A");


                            Log.e("DocPath >>", curentOrderId);

                            db.collection("OrderDetails").document(curentOrderId).update(get_data)

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(MyCartActivity.this, "Data successfully written! update", Toast.LENGTH_LONG).show();

                                            TOPIC = "/topics/" + currentUserId.toString();
                                            NOTIFICATION_TITLE = "Complete Order";
                                            NOTIFICATION_MESSAGE = "Your Order has been completed. Click here to see details";

                                            JSONObject notification = new JSONObject();
                                            JSONObject notifcationBody = new JSONObject();
                                            try {
                                                notifcationBody.put("title", NOTIFICATION_TITLE);
                                                notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                                notifcationBody.put("orderId", curentOrderId);

                                                notification.put("to", TOPIC);
                                                notification.put("data", notifcationBody);
                                            } catch (JSONException e) {
                                                Log.e(TAG, "onCreate: " + e.getMessage());
                                            }
                                            sendNotification(notification);


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressHUD.dismiss();
                                            Toast.makeText(MyCartActivity.this, "Data writing Error update", Toast.LENGTH_LONG).show();

                                        }
                                    });
                        }
                    }

                } else {

                    progressHUD.show();
                    Random rand = new Random();
                    int n = rand.nextInt(10000 - 1000);
                    final String docPath = "QF" + n;

                    Map<String, Object> get_data = new HashMap<>();
                    get_data.put("Status", "P");
                    get_data.put("userId", prefs.getString("loggedUserId", ""));
                    get_data.put("Total", String.valueOf(totalValueForAllCart));
                    get_data.put("TotalItems", myCartList.size());


                    for (int i = 0; i < myCartList.size(); i++) {
                        String data = myCartList.get(i).getFoodId() + ",_" + myCartList.get(i).getFoodName() + ",_" + myCartList.get(i).getFoodPrice() + ",_" + myCartList.get(i).getItemImage() + ",_" + myCartList.get(i).getAddersAndSizes();
                        get_data.put("Item_" + i, data);
                    }

                    Log.e("DocPath >>", docPath);

                    db.collection("OrderDetails").document(docPath).set(get_data)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(MyCartActivity.this, "Data successfully written! update", Toast.LENGTH_LONG).show();

                                    TOPIC = "/topics/ToADMIN";
                                    NOTIFICATION_TITLE = "New Order";
                                    NOTIFICATION_MESSAGE = "New Order received. Please approve or reject it.";

                                    JSONObject notification = new JSONObject();
                                    JSONObject notifcationBody = new JSONObject();
                                    try {
                                        notifcationBody.put("title", NOTIFICATION_TITLE);
                                        notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                        notifcationBody.put("orderId", docPath);

                                        notification.put("to", TOPIC);
                                        notification.put("data", notifcationBody);
                                    } catch (JSONException e) {
                                        Log.e(TAG, "onCreate: " + e.getMessage());
                                    }
                                    sendNotification(notification);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressHUD.dismiss();
                                    Toast.makeText(MyCartActivity.this, "Data writing Error update", Toast.LENGTH_LONG).show();

                                }
                            });

                }
            }
        });
    }


    private void setDatalist() {
        progressHUD.show();
        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("View Cart - " + curentOrderId);

        db.collection("OrderDetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Doc", document.getId() + " => " + document.getData());

                                if (document.getId().equals(curentOrderId)) {
                                    orderStatus = document.getString("Status");
                                    if (isVendorLogged.equals("")) {
                                        if (orderStatus.equals("C")) {
                                            placeOrderBtn.setText("Generate QR Code");
                                        } else {
                                            placeOrderBtn.setVisibility(View.GONE);
                                        }
                                    } else {
                                        if (orderStatus.equals("A")) {
                                            placeOrderBtn.setText("Complete Order");
                                        } else if (orderStatus.equals("P")) {
                                            placeOrderBtn.setText("Approve Order");
                                        } else {
                                            placeOrderBtn.setVisibility(View.GONE);
                                        }
                                    }

                                    Double totalItems = document.getDouble("TotalItems");
                                    CartDetails newCartData;
                                    List<CartDetails> newCartList;
                                    newCartList = new ArrayList<>();

                                    for (int i = 0; i < totalItems; i++) {
                                        String data = document.getString("Item_" + i);

                                        assert data != null;
                                        String foodId = data.substring(0, data.indexOf(",_"));
                                        String s1remainder = data.substring(data.indexOf(",_") + 2);

                                        String foodName = s1remainder.substring(0, s1remainder.indexOf(",_"));
                                        String s2remainder = s1remainder.substring(s1remainder.indexOf(",_") + 2);

                                        String foodPrice = s2remainder.substring(0, s2remainder.indexOf(",_"));
                                        String s3remainder = s2remainder.substring(s2remainder.indexOf(",_") + 2);

                                        String foodImage = s3remainder.substring(0, s3remainder.indexOf(",_"));
                                        String s4remainder = s3remainder.substring(s3remainder.indexOf(",_") + 2);

                                        newCartData = new CartDetails(foodId, foodName, foodPrice, foodImage, s4remainder);
                                        newCartList.add(newCartData);
                                    }


                                    CartViewAdapter myAdapter = new CartViewAdapter(MyCartActivity.this, newCartList, false);
                                    mRecycleView.setAdapter(myAdapter);

                                    mTxtTotalForCart.setText(document.getString("Total"));
                                }

                                progressHUD.dismiss();
                            }


                        } else {
                            Log.d("Doc", "Error getting documents: ", task.getException());
                            progressHUD.dismiss();
                        }
                    }
                });
    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (myCartList != null) {
                            myCartList.clear();
                            CartViewAdapter myAdapter = new CartViewAdapter(MyCartActivity.this, myCartList, true);
                            mRecycleView.setAdapter(myAdapter);
                        }

                        progressHUD.dismiss();
                        Toast.makeText(MyCartActivity.this, "Successfully sent", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onResponse: " + response.toString());
                        Intent intent = new Intent(MyCartActivity.this, OrderQueue.class);
                        startActivity(intent);
                        finish();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressHUD.dismiss();
                        Toast.makeText(MyCartActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public static void method() {
        totalValueForAllCart = 0.0;
        for (int i = 0; i < myCartList.size(); i++) {
            double addonInInt = Double.parseDouble(myCartList.get(i).getFoodPrice());
            totalValueForAllCart = totalValueForAllCart + addonInInt;
        }

        mTxtTotalForCart.setText(String.valueOf(totalValueForAllCart));
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
