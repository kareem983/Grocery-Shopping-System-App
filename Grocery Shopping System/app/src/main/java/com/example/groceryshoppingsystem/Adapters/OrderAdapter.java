package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.R;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<MyorderModel> orderItemList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView orderDate, orderNums, orderPrice, orderProducts;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderNums = itemView.findViewById(R.id.orderNums);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderProducts = itemView.findViewById(R.id.orderProducts);
        }
    }


    public OrderAdapter(Context context,List<MyorderModel> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItemview = LayoutInflater.from(context).inflate(R.layout.order_itemlayout, parent, false);
        return new ViewHolder(orderItemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyorderModel model = orderItemList.get(position);

        holder.orderDate.setText(model.getDate());
        holder.orderNums.setText(model.getOrderNums());
        holder.orderPrice.setText(model.getOrderPrice());
        holder.orderProducts.setText(model.getOrderProducts());
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

}
