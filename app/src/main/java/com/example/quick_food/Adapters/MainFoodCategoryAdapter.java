package com.example.quick_food.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.GetterSetters.FoodData;
import com.example.quick_food.R;
import com.example.quick_food.recycler.FoodsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MainFoodCategoryAdapter extends RecyclerView.Adapter<MainFoodViewHolder> {


    Context mContext;
    List<FoodData> myFoodList;


    public MainFoodCategoryAdapter(Context mContext, List<FoodData> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @Override
    public MainFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_row_it, viewGroup, false);

        return new MainFoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainFoodViewHolder foodViewHolder, final int i) {
        final String idOfFood = myFoodList.get(i).getId();

        String foodImage = myFoodList.get(i).getItemImage();
        Picasso.get()
                .load(foodImage)
                .placeholder(R.drawable.walking_food)
                .into(foodViewHolder.imageView);

        foodViewHolder.mTitle.setText(myFoodList.get(i).getItemName());
        foodViewHolder.mDescription.setText(myFoodList.get(i).getItemDescription());


        foodViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FoodsActivity.class);
                intent.putExtra("EXTRA_FOOD_ID", idOfFood);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }
}

class MainFoodViewHolder extends RecyclerView.ViewHolder {


    ImageView imageView;
    TextView mTitle, mDescription;
    CardView mCardView;


    public MainFoodViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mDescription = itemView.findViewById(R.id.tvDescription);
        mCardView = itemView.findViewById(R.id.myCardView);

    }
}
