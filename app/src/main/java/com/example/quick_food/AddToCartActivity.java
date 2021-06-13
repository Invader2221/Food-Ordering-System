package com.example.quick_food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quick_food.recycler.MyCartActivity;
import com.squareup.picasso.Picasso;

import static com.example.quick_food.Utils.selectedFoodItem;

public class AddToCartActivity extends AppCompatActivity {

    TextView mTitle, mTxtOneOne, mTxtOneTwo, mTxtOneThree, mTxtTwoOne, mTxtTwoTwo, mTxtTwoThree, mTxtTotal;
    ImageView imageViewCart;
    Button buttoAddCart;
    RadioGroup radioGroupOne, radioGroupTwo;
    LinearLayout adderLayout, sizeLayout;
    Spinner spinnerCount;
    RadioButton sizeOneRB, sizeTwoRB, sizeThreeRB, adderOneRB, adderTwoRB, adderThreeRB;

    String[] size = {"1", "2", "3", "4"};
    ArrayAdapter<String> adapter;

    double totalValue, groupOneVal, groupTwoVal, priceDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);

        final String foodImage = getIntent().getStringExtra("FOOD_IMAGE_FOR_CART");
        final String foodId = getIntent().getStringExtra("FOOD_ID_FOR_CART");
        final String foodName = getIntent().getStringExtra("FOOD_NAME_FOR_CART");
        final String price = getIntent().getStringExtra("FOOD_PRICE_FOR_CART");

        mTitle = findViewById(R.id.txt_cart_product_name);
        mTxtOneOne = findViewById(R.id.text_option_one_one);
        mTxtOneTwo = findViewById(R.id.text_option_one_two);
        mTxtOneThree = findViewById(R.id.text_option_one_thre);
        mTxtTwoOne = findViewById(R.id.text_option_two_one);
        mTxtTwoTwo = findViewById(R.id.text_option_two_two);
        mTxtTwoThree = findViewById(R.id.text_option_two_three);
        mTxtTotal = findViewById(R.id.total_txt);
        imageViewCart = findViewById(R.id.img_cart_product);
        buttoAddCart = findViewById(R.id.cart_submit);
        spinnerCount = (Spinner) findViewById(R.id.spinner_count);
        sizeLayout = findViewById(R.id.layout_size);
        adderLayout = findViewById(R.id.layout_adder);
        sizeOneRB = findViewById(R.id.fd_sizeN);
        sizeTwoRB = findViewById(R.id.fd_sizeM);
        sizeThreeRB = findViewById(R.id.fd_sizeL);
        adderOneRB = findViewById(R.id.f_add_none);
        adderTwoRB = findViewById(R.id.f_add_cheese);
        adderThreeRB = findViewById(R.id.f_add_chicken);

        priceDouble = Double.parseDouble(price);

        mTitle.setText(foodName);
        mTxtTotal.setText(String.valueOf(priceDouble));

        Picasso.get()
                .load(foodImage)
                .placeholder(R.drawable.walking_food)
                .into(imageViewCart);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, size);
        spinnerCount.setAdapter(adapter);

        radioGroupOne = (RadioGroup) findViewById(R.id.radio_group_one);
        radioGroupTwo = (RadioGroup) findViewById(R.id.radio_group_two);


        String sizeOne = selectedFoodItem.getSizeOne();
        String sizeTwo = selectedFoodItem.getSizeTwo();
        String sizeThree = selectedFoodItem.getSizeThree();


        String adderOne = selectedFoodItem.getAdderOne();
        String adderTwo = selectedFoodItem.getAdderTwo();
        String adderThree = selectedFoodItem.getAdderThree();


        if (sizeOne.equals("-")) {
            sizeLayout.setVisibility(View.GONE);
        } else if (sizeTwo.equals("-")) {

            String s1kept = sizeOne.substring(0, sizeOne.indexOf(","));
            String s1remainder = sizeOne.substring(sizeOne.indexOf(",") + 1);

            sizeTwoRB.setVisibility(View.GONE);
            mTxtOneTwo.setVisibility(View.GONE);

            sizeThreeRB.setVisibility(View.GONE);
            mTxtOneThree.setVisibility(View.GONE);

            sizeOneRB.setText(s1kept);
            mTxtOneOne.setText(s1remainder);
        } else if (sizeThree.equals("-")) {
            sizeThreeRB.setVisibility(View.GONE);
            mTxtOneThree.setVisibility(View.GONE);


            String s1kept = sizeOne.substring(0, sizeOne.indexOf(","));
            String s1remainder = sizeOne.substring(sizeOne.indexOf(",") + 1);

            String s2kept = sizeTwo.substring(0, sizeTwo.indexOf(","));
            String s2remainder = sizeTwo.substring(sizeTwo.indexOf(",") + 1);


            sizeOneRB.setText(s1kept);
            sizeTwoRB.setText(s2kept);
            mTxtOneOne.setText(s1remainder);
            mTxtOneTwo.setText(s2remainder);

        } else {

            String s1kept = sizeOne.substring(0, sizeOne.indexOf(","));
            String s1remainder = sizeOne.substring(sizeOne.indexOf(",") + 1);

            String s2kept = sizeTwo.substring(0, sizeTwo.indexOf(","));
            String s2remainder = sizeTwo.substring(sizeTwo.indexOf(",") + 1);

            String s3kept = sizeThree.substring(0, sizeThree.indexOf(","));
            String s3remainder = sizeThree.substring(sizeThree.indexOf(",") + 1);


            sizeOneRB.setText(s1kept);
            sizeTwoRB.setText(s2kept);
            sizeThreeRB.setText(s3kept);

            mTxtOneOne.setText(s1remainder);
            mTxtOneTwo.setText(s2remainder);
            mTxtOneThree.setText(s3remainder);
        }


        if (adderOne.equals("-")) {
            adderLayout.setVisibility(View.GONE);
        } else if (adderTwo.equals("-")) {

            String s1kept = adderOne.substring(0, adderOne.indexOf(","));
            String s1remainder = adderOne.substring(adderOne.indexOf(",") + 1);

            adderTwoRB.setVisibility(View.GONE);
            mTxtTwoTwo.setVisibility(View.GONE);

            adderThreeRB.setVisibility(View.GONE);
            mTxtTwoThree.setVisibility(View.GONE);

            adderOneRB.setText(s1kept);
            mTxtTwoOne.setText(s1remainder);
        } else if (adderThree.equals("-")) {
            adderThreeRB.setVisibility(View.GONE);
            mTxtTwoThree.setVisibility(View.GONE);


            String s1kept = adderOne.substring(0, adderOne.indexOf(","));
            String s1remainder = adderOne.substring(adderOne.indexOf(",") + 1);

            String s2kept = adderTwo.substring(0, adderTwo.indexOf(","));
            String s2remainder = adderTwo.substring(adderTwo.indexOf(",") + 1);


            adderOneRB.setText(s1kept);
            adderTwoRB.setText(s2kept);
            mTxtTwoOne.setText(s1remainder);
            mTxtTwoTwo.setText(s2remainder);

        } else {

            String s1kept = adderOne.substring(0, adderOne.indexOf(","));
            String s1remainder = adderOne.substring(adderOne.indexOf(",") + 1);

            String s2kept = adderTwo.substring(0, adderTwo.indexOf(","));
            String s2remainder = adderTwo.substring(adderTwo.indexOf(",") + 1);

            String s3kept = adderThree.substring(0, adderThree.indexOf(","));
            String s3remainder = adderThree.substring(adderThree.indexOf(",") + 1);


            adderOneRB.setText(s1kept);
            adderTwoRB.setText(s2kept);
            adderThreeRB.setText(s3kept);

            mTxtTwoOne.setText(s1remainder);
            mTxtTwoTwo.setText(s2remainder);
            mTxtTwoThree.setText(s3remainder);
        }

        radioGroupOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.fd_sizeN) {

                    groupOneVal = Double.parseDouble(String.valueOf(mTxtOneOne.getText()));
                    totalValue = groupOneVal + groupTwoVal + priceDouble;
                    mTxtTotal.setText(String.valueOf(totalValue));
                } else if (checkedId == R.id.fd_sizeM) {

                    groupOneVal = Double.parseDouble(String.valueOf(mTxtOneTwo.getText()));
                    totalValue = groupOneVal + groupTwoVal + priceDouble;
                    mTxtTotal.setText(String.valueOf(totalValue));
                } else {
                    groupOneVal = Double.parseDouble(String.valueOf(mTxtOneThree.getText()));
                    totalValue = groupOneVal + groupTwoVal + priceDouble;
                    mTxtTotal.setText(String.valueOf(totalValue));
                }
            }
        });

        radioGroupTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.f_add_cheese) {
                    groupTwoVal = Double.parseDouble(String.valueOf(mTxtTwoTwo.getText()));
                    totalValue = groupOneVal + groupTwoVal + priceDouble;
                    mTxtTotal.setText(String.valueOf(totalValue));
                } else if (checkedId == R.id.f_add_chicken) {
                    groupTwoVal = Double.parseDouble(String.valueOf(mTxtTwoThree.getText()));
                    totalValue = groupOneVal + groupTwoVal + priceDouble;
                    mTxtTotal.setText(String.valueOf(totalValue));
                } else {
                    groupTwoVal = Double.parseDouble(String.valueOf(mTxtTwoOne.getText()));
                    totalValue = groupOneVal + groupTwoVal + priceDouble;
                    mTxtTotal.setText(String.valueOf(totalValue));
                }
            }
        });

        buttoAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double finalPrice;
                if (totalValue == 0) {
                    finalPrice = priceDouble;
                } else {
                    finalPrice = totalValue;
                }

                String sizeSelector = "";

                if (sizeLayout.getVisibility() == View.VISIBLE) {
                    int selectedId = radioGroupOne.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    sizeSelector = sizeSelector + (String) radioButton.getText();
                }

                if (adderLayout.getVisibility() == View.VISIBLE) {
                    int selectedId = radioGroupTwo.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    sizeSelector = sizeSelector + ", "+ (String) radioButton.getText();
                }

                Intent intent = new Intent(AddToCartActivity.this, MyCartActivity.class);
                intent.putExtra("image_Name", foodImage);
                intent.putExtra("total_price", String.valueOf(finalPrice));
                intent.putExtra("item_name", foodName);
                intent.putExtra("item_id", foodId);
                intent.putExtra("selected_adders_sizes", sizeSelector);
                startActivity(intent);
                finish();
            }
        });
    }

}
