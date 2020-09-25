package com.example.groceryshoppingsystem.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter {

    private List<MyorderModel> orderItemList;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_itemlayout, parent, false);
        return new orderItemViewHolder(orderItemview);
    }

    public OrderAdapter(List<MyorderModel> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String Date = orderItemList.get(position).getDate();
        String Nums = orderItemList.get(position).getOrderNums();
        String Price = orderItemList.get(position).getOrderPrice();
        String Products = orderItemList.get(position).getOrderProducts();

        ((orderItemViewHolder) holder).setItemDetails(Date, Nums, Price, Products);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }


    class orderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView orderDate, orderNums, orderPrice, orderProducts;
        private DatabaseReference root;
        private FirebaseAuth mAuth;
        private String CurrentUser;

        public orderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            root = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            CurrentUser = mAuth.getCurrentUser().getUid();
            orderDate = itemView.findViewById(R.id.orderDate);
            orderNums = itemView.findViewById(R.id.orderNums);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderProducts = itemView.findViewById(R.id.orderProducts);
        }

        private void setItemDetails(String Date, String Nums, String Price, String Products) {
            orderDate.setText(Date);
            orderNums.setText(Nums);
            orderPrice.setText(Price+" EGP");
            orderProducts.setText(Products);
        }

    }

}

