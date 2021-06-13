package com.example.quick_food.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.GetterSetters.CartDetails;
import com.example.quick_food.R;
import com.example.quick_food.recycler.MyCartActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewHolder> {


    Context mContext;
    List<CartDetails> myCartList;
    Boolean fromMyCart;


    public CartViewAdapter(Context mContext, List<CartDetails> myCartList, Boolean fromMyCart) {
        this.mContext = mContext;
        this.myCartList = myCartList;
        this.fromMyCart = fromMyCart;
    }

    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_confirm, viewGroup, false);

        return new CartViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder CardViewHolder, final int i) {

        if(!fromMyCart){
            CardViewHolder.imageDelete.setVisibility(View.GONE);
        }

        final String nameOfFood = myCartList.get(i).getFoodName();

        CardViewHolder.mTitle.setText(nameOfFood);
        CardViewHolder.mPrice.setText(myCartList.get(i).getFoodPrice());

        String adderSizeText = myCartList.get(i).getAddersAndSizes();

        if (adderSizeText.equals("")) {
            CardViewHolder.mAdderText.setVisibility(View.GONE);
        } else {
            CardViewHolder.mAdderText.setText(adderSizeText);
        }

        String foodImage = myCartList.get(i).getItemImage();
        Picasso.get()
                .load(foodImage)
                .placeholder(R.drawable.walking_food)
                .into(CardViewHolder.imageView);

        CardViewHolder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCartList.remove(i);
                MyCartActivity.method();
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return myCartList.size();
    }
}


class CartViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView, imageDelete;
    TextView mTitle, mPrice, mAdderText;


    public CartViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.photo_thumb);
        mTitle = itemView.findViewById(R.id.tite_text);
        mPrice = itemView.findViewById(R.id.category_text);
        imageDelete = itemView.findViewById(R.id.delete_image);
        mAdderText = itemView.findViewById(R.id.size_adder_text);

    }
}


