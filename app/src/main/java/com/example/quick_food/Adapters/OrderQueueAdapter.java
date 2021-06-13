package com.example.quick_food.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quick_food.GetterSetters.OrderDetails;
import com.example.quick_food.R;
import com.example.quick_food.recycler.MyCartActivity;

import java.util.List;

public class OrderQueueAdapter extends RecyclerView.Adapter<OrderQueueHolder> {


    Context mContext;
    private final List<OrderDetails> myOrderDetails;

    public OrderQueueAdapter(Context mContext, List<OrderDetails> myOrderDetails) {
        this.mContext = mContext;
        this.myOrderDetails = myOrderDetails;
    }


    @Override
    public OrderQueueHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_row_view, viewGroup, false);

        return new OrderQueueHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull final OrderQueueHolder orderFinder, final int i) {

        final String OrderId = myOrderDetails.get(i).getOrderId();
        final String orderStatus = myOrderDetails.get(i).getStatus();

        switch (orderStatus) {
            case "C":
                orderFinder.orderStatus.setText("Complete");
                orderFinder.orderStatus.setTextColor(Color.parseColor("#2fb55e"));
                break;
            case "P":
                orderFinder.orderStatus.setText("Pending");
                orderFinder.orderStatus.setTextColor(Color.parseColor("#2f4eb5"));
                break;
            case "A":
                orderFinder.orderStatus.setText("Approved");
                orderFinder.orderStatus.setTextColor(Color.parseColor("#792fb5"));
                break;
            case "R":
                orderFinder.orderStatus.setText("Rejected");
                orderFinder.orderStatus.setTextColor(Color.parseColor("#b52f62"));
                break;
            case "D":
                orderFinder.orderStatus.setText("Done");
                orderFinder.orderStatus.setTextColor(Color.parseColor("#b5b52f"));
                break;
            default:
                orderFinder.orderStatus.setText(orderStatus);
                break;
        }

        orderFinder.OrderId.setText(OrderId);
        orderFinder.totalItems.setText("Items : " + myOrderDetails.get(i).getTotalItems());
        orderFinder.totalText.setText("Total : " + myOrderDetails.get(i).getTotalCost() + " RS");


        orderFinder.hCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyCartActivity.class);
                intent.putExtra("EXTRA_ORDER_ID", OrderId);
                intent.putExtra("EXTRA_ORDER_USER_ID", myOrderDetails.get(i).getUserId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myOrderDetails.size();
    }
}


class OrderQueueHolder extends RecyclerView.ViewHolder {

    TextView orderStatus, OrderId, totalItems, totalText;
    CardView hCardView;

    public OrderQueueHolder(View itemView) {
        super(itemView);

        orderStatus = itemView.findViewById(R.id.order_status_text);
        OrderId = itemView.findViewById(R.id.order_id_text);
        totalItems = itemView.findViewById(R.id.order_items_text);
        totalText = itemView.findViewById(R.id.order_total_text);
        hCardView = itemView.findViewById(R.id.orderDetailsCard);

    }
}
