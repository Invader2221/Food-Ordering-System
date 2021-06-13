package com.example.quick_food.recycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.Adapters.MainFoodCategoryAdapter;
import com.example.quick_food.GetterSetters.FoodData;
import com.example.quick_food.QRCodeScanner;
import com.example.quick_food.R;
import com.example.quick_food.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import static com.example.quick_food.Login.MY_PREFS_NAME;

public class MainFoodCategoryActivity extends AppCompatActivity {


    RecyclerView mRecycleView;
    List<FoodData> myFoodList;
    FoodData mFoodData;
    private boolean userIsVender = false;
    FirebaseFirestore db;
    FirebaseStorage storage;
    KProgressHUD progressHUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);
        getSupportActionBar().setTitle("Welcome To Store");

        progressHUD = KProgressHUD.create(MainFoodCategoryActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        storage = FirebaseStorage.getInstance();

        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainFoodCategoryActivity.this, 1);
        mRecycleView.setLayoutManager(gridLayoutManager);

        setDatalist();


    }

    private void setDatalist() {
        progressHUD.show();
        db = FirebaseFirestore.getInstance();
        myFoodList = new ArrayList<>();

        db.collection("Main Food Types")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("Doc", document.getId() + " => " + document.getData());

                                final String Title = document.getString("name");
                                final String id = document.getString("id");
                                final String image = document.getString("image");
                                final String description = document.getString("Description");

                                StorageReference storageRef = storage.getReference();
                                StorageReference downloadRef = storageRef.child("Main Food Types/" + image);
                                downloadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("Doc1", " => " + uri);
                                        String imageURI = uri.toString();

                                        mFoodData = new FoodData(id, Title, description, imageURI);
                                        myFoodList.add(mFoodData);

                                        MainFoodCategoryAdapter foodCategoryAdapter = new MainFoodCategoryAdapter(MainFoodCategoryActivity.this, myFoodList);
                                        mRecycleView.setAdapter(foodCategoryAdapter);
                                        progressHUD.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        //   Toast.makeText(getActivity(), "Error getting Data", Toast.LENGTH_LONG).show();
                                        Log.d("Doc", "Error getting Data: ");
                                        progressHUD.dismiss();
                                    }
                                });

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
        MenuItem OrderQueue = menu.findItem(R.id.Orders);

        if (!theVender.equals("")) {
            userIsVender = true;

            QRScanner.setVisible(true);
            OrderQueue.setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cart_icon_menu) {
            Intent intent = new Intent(MainFoodCategoryActivity.this, MyCartActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.Orders) {
            Intent intent = new Intent(MainFoodCategoryActivity.this, OrderQueue.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile_menu) {
            Intent intent = new Intent(MainFoodCategoryActivity.this, UserProfile.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.QR_scanner) {
            Intent intent = new Intent(MainFoodCategoryActivity.this, QRCodeScanner.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
