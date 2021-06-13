package com.example.quick_food.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.AddToCartActivity;
import com.example.quick_food.GetterSetters.FoodDetails;
import com.example.quick_food.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.quick_food.Utils.selectedFoodItem;

public class FoodDetailAdapter extends RecyclerView.Adapter<FoodDeailHolder> {

    private Context fdContext;
    private List<FoodDetails> myFoodDetailList;
    private boolean fromEdit;

    public FoodDetailAdapter(Context fdContext, List<FoodDetails> myFoodDetailList, boolean fromEdit) {
        this.fdContext = fdContext;
        this.myFoodDetailList = myFoodDetailList;
        this.fromEdit = fromEdit;
    }

    @Override
    public FoodDeailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View fView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_item_layout, viewGroup, false);

        return new FoodDeailHolder(fView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDeailHolder foodDeailHolder, final int i) {

        final String foodID = myFoodDetailList.get(i).getFoodId();
        final String foodName = myFoodDetailList.get(i).getFoodName();
        final String foodPrice = myFoodDetailList.get(i).getFoodPrice();

        final String foodImage = myFoodDetailList.get(i).getItemImage();
        Picasso.get()
                .load(foodImage)
                .placeholder(R.drawable.walking_food)
                .into(foodDeailHolder.fimageView);

        foodDeailHolder.fdTitle.setText(foodName);
        foodDeailHolder.fdPrice.setText(foodPrice);

        if (fromEdit) {
            foodDeailHolder.maddToCard.setText("Delete");
        }

        foodDeailHolder.maddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!fromEdit) {
                    selectedFoodItem = myFoodDetailList.get(i);

                    Intent intent = new Intent(fdContext, AddToCartActivity.class);
                    intent.putExtra("FOOD_ID_FOR_CART", foodID);
                    intent.putExtra("FOOD_NAME_FOR_CART", foodName);
                    intent.putExtra("FOOD_IMAGE_FOR_CART", foodImage);
                    intent.putExtra("FOOD_PRICE_FOR_CART", foodPrice);
                    fdContext.startActivity(intent);

                } else {


                    final KProgressHUD progressHUD = KProgressHUD.create(fdContext)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setDetailsLabel("We are creating your food item.")
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f);
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();

                    progressHUD.show();
                    db.collection("Foods").document(foodID).delete()

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressHUD.dismiss();
                                    myFoodDetailList.remove(i);
                                    notifyDataSetChanged();
                                    Toast.makeText(fdContext, "Data successfully written! update", Toast.LENGTH_LONG).show();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressHUD.dismiss();
                                    Toast.makeText(fdContext, "Data writing Error update", Toast.LENGTH_LONG).show();

                                }
                            });
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return myFoodDetailList.size();
    }
}

class FoodDeailHolder extends RecyclerView.ViewHolder {

    ImageView fimageView;
    TextView fdTitle, fdPrice;
    CardView fdCardView;
    Button maddToCard;


    public FoodDeailHolder(@NonNull View itemView) {
        super(itemView);

        fimageView = (ImageView) itemView.findViewById(R.id.fdImage);
        fdTitle = (TextView) itemView.findViewById(R.id.foTitle);
        fdPrice = (TextView) itemView.findViewById(R.id.foPrice);
        maddToCard = itemView.findViewById(R.id.btn_add_cart);
        fdCardView = (CardView) itemView.findViewById(R.id.myFoodCardView);

    }
}
