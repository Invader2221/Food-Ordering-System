package com.example.quick_food;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FoodItemEditActivity extends AppCompatActivity {

    Button addSizesButton, addAdderButton, buttonAddNow;
    LinearLayout foodSizeLayoutList, foodAdderLayout;
    List<String> foodSizeList = new ArrayList<>();
    int addButtonCount = 0;
    int sizeButtonCount = 0;
    KProgressHUD progressHUD;
    FirebaseFirestore db;
    EditText foodNameEditText, foodTypeEditText, foodPriceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        getSupportActionBar().setTitle("Add Food Items");

        foodSizeLayoutList = findViewById(R.id.sizeSelect);
        addSizesButton = findViewById(R.id.addAvailableSizes);

        foodAdderLayout = findViewById(R.id.adderSelect);
        addAdderButton = findViewById(R.id.addAvailableAdders);

        buttonAddNow = findViewById(R.id.button_add_now);

        foodNameEditText = findViewById(R.id.food_name);
        foodTypeEditText = findViewById(R.id.food_type);
        foodPriceEditText = findViewById(R.id.food_price);

        foodSizeList.add("Regular");
        foodSizeList.add("Medium");
        foodSizeList.add("Large");

        progressHUD = KProgressHUD.create(FoodItemEditActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("We are creating your food item.")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        db = FirebaseFirestore.getInstance();

        addAdderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddderView();
                addButtonCount++;
                if (addButtonCount == 3) {
                    addAdderButton.setVisibility(View.GONE);
                }
            }

        });

        addSizesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
                sizeButtonCount++;
                if (sizeButtonCount == 3) {
                    addSizesButton.setVisibility(View.GONE);
                }
            }

        });

        buttonAddNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();

            }

        });
    }


    private void addAddderView() {

        final View foodAdder = getLayoutInflater().inflate(R.layout.raw_add_adders, null, false);
        EditText editAdderName = (EditText) foodAdder.findViewById(R.id.adderName);
        EditText editAdderPrice = (EditText) foodAdder.findViewById(R.id.adderPrice);
        ImageView imageAdderClose = (ImageView) foodAdder.findViewById(R.id.closeAdderImage);


        imageAdderClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAdderView(foodAdder);


            }
        });

        foodAdderLayout.addView(foodAdder);
    }

    private void removeAdderView(View view) {
        foodAdderLayout.removeView(view);
        addButtonCount--;
        if (addButtonCount == 2) {
            addAdderButton.setVisibility(View.VISIBLE);
        }
    }


    private void addView() {

        //foodSizes
        final View foodSize = getLayoutInflater().inflate(R.layout.raw_add_sizes, null, false);
        AppCompatSpinner spinnerSize = (AppCompatSpinner) foodSize.findViewById(R.id.foodSizeSpinner);
        EditText editSizePrice = (EditText) foodSize.findViewById(R.id.sizePrice);
        ImageView imageSizeClose = (ImageView) foodSize.findViewById(R.id.closeSizeImage);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, foodSizeList);
        spinnerSize.setAdapter(arrayAdapter);

        imageSizeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSizeView(foodSize);
            }
        });
        foodSizeLayoutList.addView(foodSize);

    }

    private void removeSizeView(View view) {

        foodSizeLayoutList.removeView(view);
        sizeButtonCount--;
        if (sizeButtonCount == 2) {
            addSizesButton.setVisibility(View.VISIBLE);
        }


    }

    private void sendData() {

        boolean errorOnSize = false;
        boolean errorOnAdder = false;
        final int childCount = foodSizeLayoutList.getChildCount();
        final int childCountAdd = foodAdderLayout.getChildCount();

        for (int i = 0; i < childCount; i++) {
            LinearLayout v = (LinearLayout) foodSizeLayoutList.getChildAt(i);

            EditText twoText = (EditText) v.getChildAt(1);
            if (twoText.getText().toString().equals("")) {
                errorOnSize = true;
            }
        }

        for (int i = 0; i < childCountAdd; i++) {
            LinearLayout v = (LinearLayout) foodAdderLayout.getChildAt(i);
            EditText OneText = (EditText) v.getChildAt(1);
            EditText twoText = (EditText) v.getChildAt(1);
            if (twoText.getText().toString().equals("") || OneText.getText().toString().equals("")) {
                errorOnAdder = true;
            }
        }

        if (foodNameEditText.getText().toString().equals("")) {
            showAlert("Please enter food name");
        } else if (foodTypeEditText.getText().toString().equals("")) {
            showAlert("Please enter food main type id");
        } else if (foodPriceEditText.getText().toString().equals("")) {
            showAlert("Please enter food price");
        } else if (errorOnSize) {
            showAlert("Please enter all remains food size prices");
        } else if (errorOnAdder) {
            showAlert("Please enter all remains food adders prices and names");
        } else {

            progressHUD.show();
            Random rand = new Random();
            int n = rand.nextInt(10000 - 10);
            final String docPath = "" + n;

            Map<String, Object> get_data = new HashMap<>();
            get_data.put("id", docPath);
            get_data.put("Type", foodTypeEditText.getText().toString());
            get_data.put("price", foodPriceEditText.getText().toString());
            get_data.put("name", foodNameEditText.getText().toString());
            get_data.put("image", "softdrinks.jpg");


            String SizeOne = "-";
            String SizeTwo = "-";
            String Sizethree = "-";
            for (int i = 0; i < childCount; i++) {
                LinearLayout v = (LinearLayout) foodSizeLayoutList.getChildAt(i);

                FrameLayout oneText = (FrameLayout) v.getChildAt(0);
                AppCompatSpinner spanner = (AppCompatSpinner) oneText.getChildAt(0);
                String oneSelected = spanner.getSelectedItem().toString();

                EditText twoText = (EditText) v.getChildAt(1);

                if (i == 0) {
                    SizeOne = oneSelected + "," + twoText.getText().toString();
                } else if (i == 1) {
                    SizeTwo = oneSelected + "," + twoText.getText().toString();
                } else {
                    Sizethree = oneSelected + "," + twoText.getText().toString();
                }
            }
            get_data.put("SizeOne", SizeOne);
            get_data.put("SizeTwo", SizeTwo);
            get_data.put("SizeThree", Sizethree);

            String adderOne = "-";
            String adderTwo = "-";
            String adderThree = "-";
            for (int i = 0; i < childCountAdd; i++) {
                LinearLayout v = (LinearLayout) foodAdderLayout.getChildAt(i);
                EditText oneText = (EditText) v.getChildAt(0);
                EditText twoText = (EditText) v.getChildAt(1);
                if (i == 0) {
                    adderOne = oneText.getText().toString() + "," + twoText.getText().toString();
                } else if (i == 1) {
                    adderTwo = oneText.getText().toString() + "," + twoText.getText().toString();
                } else {
                    adderThree = oneText.getText().toString() + "," + twoText.getText().toString();
                }
            }
            get_data.put("AdderOne", adderOne);
            get_data.put("AdderTwo", adderTwo);
            get_data.put("AdderThree", adderThree);


            Log.e("DocPath >>", docPath);

            db.collection("Foods").document(docPath).set(get_data)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressHUD.dismiss();
                            Toast.makeText(FoodItemEditActivity.this, "Data successfully written! update", Toast.LENGTH_LONG).show();
                            finish();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressHUD.dismiss();
                            Toast.makeText(FoodItemEditActivity.this, "Data writing Error update", Toast.LENGTH_LONG).show();

                        }
                    });

        }
    }

    void showAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FoodItemEditActivity.this);

        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
        ;

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}