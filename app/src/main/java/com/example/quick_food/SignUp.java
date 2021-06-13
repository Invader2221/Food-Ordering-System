package com.example.quick_food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private ImageView CreateAccountButton;
    private EditText InputName, InputStudentId, InputUserMobile, InputPassword, InputEmail;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        CreateAccountButton = (ImageView) findViewById(R.id.r_button);
        InputName = (EditText) findViewById(R.id.r_fName);
        InputStudentId = (EditText) findViewById(R.id.sid);
        InputEmail = (EditText) findViewById(R.id.txt_email);
        InputPassword = (EditText) findViewById(R.id.r_password);
        InputUserMobile = (EditText) findViewById(R.id.r_mobile_number) ;
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {

        String name = InputName.getText().toString();
        String stuID = InputStudentId.getText().toString();
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();
        String mobile = InputUserMobile.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(stuID)) {
            Toast.makeText(this, "Please enter your student ID", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidaDetails(name, stuID, mobile, email, password);
        }

    }

    private void ValidaDetails(final String name, final String stuID, final String mobile, final String email, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(stuID).exists())) {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);
                    userdataMap.put("stuId", stuID);
                    userdataMap.put("mobile", mobile);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(stuID).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Congratulations, your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(SignUp.this, Login.class);
                                        startActivity(intent);


                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(SignUp.this, "Network Error, please try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                } else {
                    Toast.makeText(SignUp.this, "Student ID " + stuID +  " is already registered", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(SignUp.this, Login.class);
                    startActivity(intent);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void btn_logInForm(View view) {

        startActivity(new Intent(getApplicationContext(), Login.class));
    }


}
