package com.example.quick_food;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_food.recycler.FoodsActivity;
import com.example.quick_food.recycler.OrderQueue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.example.quick_food.Login.MY_PREFS_NAME;

public class UserProfile extends AppCompatActivity {

    TextView userName, userId, userNumber, userEmail, orderQueueButton, itemListButton, logOutButton;
    private boolean userIsVender = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("User Details");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("FCM", "onTokenRefresh completed with token: " + token);

        userName = findViewById(R.id.prfile_name);
        userId = findViewById(R.id.prfile_user_id);
        userEmail = findViewById(R.id.prfile_email);
        userNumber = findViewById(R.id.prfile_user_number);
        logOutButton = findViewById(R.id.btn_logout);
        orderQueueButton = findViewById(R.id.buttonOrderQue);
        itemListButton = findViewById(R.id.buttonItemList);


        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userName.setText(String.valueOf(prefs.getString("loggedUserName", "")));
        userId.setText(String.valueOf(prefs.getString("loggedUserId", "")));
        userNumber.setText(String.valueOf(prefs.getString("loggedUserMobile", "")));
        userEmail.setText(String.valueOf(prefs.getString("loggedUserEmail", "")));
        String theVender = prefs.getString("userIsVender", "");

        if (!theVender.equals("")) {
            userIsVender = true;
            orderQueueButton.setVisibility(View.VISIBLE);
            itemListButton.setVisibility(View.VISIBLE);
        }

        orderQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserProfile.this, OrderQueue.class);
                startActivityForResult(intent,101);

            }
        });

        itemListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserProfile.this, FoodsActivity.class);
                startActivity(intent);


            }
        });


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserProfile.this);

                alertDialogBuilder.setTitle("Logout");
                alertDialogBuilder
                        .setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                FirebaseMessaging.getInstance().unsubscribeFromTopic(prefs.getString("loggedUserId", ""));
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("ToADMIN");

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.remove("userPhoneNumber");
                                editor.remove("loggedUserName");
                                editor.remove("loggedUserId");
                                editor.remove("userIsVender");
                                editor.remove("loggedUserMobile");
                                editor.remove("loggedUserEmail");
                                editor.apply();

                                Intent intent = new Intent(UserProfile.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
            Intent intent = new Intent(UserProfile.this, QRCodeScanner.class);
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
