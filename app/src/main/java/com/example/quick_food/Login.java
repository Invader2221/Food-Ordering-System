package com.example.quick_food;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_food.recycler.MainFoodCategoryActivity;
import com.example.quick_food.recycler.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {

    EditText email, pass;
    private DatabaseReference ref;
    Button btn_Login;
    public static final String MY_PREFS_NAME = "UserLogin";
    boolean userFound = false;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("userPhoneNumber", "");
        if (!name.equals("")) {

            String isVendorLogged = prefs.getString("userIsVender", "");
            if (isVendorLogged.equals("")) {
                startActivity(new Intent(Login.this, MainFoodCategoryActivity.class));
                finish();
            } else {
                startActivity(new Intent(Login.this, UserProfile.class));
                finish();
            }
        }

        email = (EditText) findViewById(R.id.s_mail);
        pass = (EditText) findViewById(R.id.password);
        btn_Login = findViewById(R.id.l_button);

        ref = FirebaseDatabase.getInstance().getReference();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {

                            final Users userNew = datas.getValue(Users.class);
                            String emailText = userNew.getEmail();

                            if (emailText.equals(email.getText().toString().trim())) {

                                userFound = true;
                                String password = userNew.getPassword();
                                String theVender = userNew.getAdmin();

                                if (password.equals(pass.getText().toString().trim())) {

                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("userPhoneNumber", dataSnapshot.getKey());
                                    editor.putString("loggedUserName", userNew.getName());
                                    editor.putString("loggedUserId", userNew.getStuid());
                                    editor.putString("loggedUserMobile", userNew.getMobile());
                                    editor.putString("loggedUserEmail", userNew.getEmail());

                                    Log.e("TAG", dataSnapshot.getKey());


                                    if (theVender == null) {

                                        FirebaseMessaging.getInstance().subscribeToTopic(userNew.getStuid())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        String msg = "Welcome " + userNew.getStuid();

                                                        Log.e("TAG", msg);
                                                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        Intent intent = new Intent(Login.this, MainFoodCategoryActivity.class);
                                        finish();
                                        startActivity(intent);
                                        editor.apply();

                                    } else {


                                        FirebaseMessaging.getInstance().subscribeToTopic("ToADMIN")
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        String msg = "Welcome " + userNew.getStuid();

                                                        Log.e("TAG", msg);
                                                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        editor.putString("userIsVender", "YES");

                                        Intent intent = new Intent(Login.this, UserProfile.class);
                                        finish();
                                        startActivity(intent);
                                        editor.apply();

                                    }

                                } else {
                                    Toast.makeText(Login.this, "Password is wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        if (!userFound) {
                            Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Login.this, "No internet found", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void btn_singupForm(View view) {

        startActivity(new Intent(getApplicationContext(), SignUp.class));
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
