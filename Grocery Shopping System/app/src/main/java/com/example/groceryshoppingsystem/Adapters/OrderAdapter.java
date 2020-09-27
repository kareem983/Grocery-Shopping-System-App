package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.R;
import com.example.groceryshoppingsystem.UI.ScanQRCodeActivity;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<MyorderModel> orderItemList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView orderDate, orderNums, orderPrice, orderProducts, OrderCheck;
        private Button ScanQrCode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderNums = itemView.findViewById(R.id.orderNums);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderProducts = itemView.findViewById(R.id.orderProducts);
            OrderCheck = itemView.findViewById(R.id.OrderCheck);
            ScanQrCode = itemView.findViewById(R.id.ScanQRCode);
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
        final MyorderModel model = orderItemList.get(position);

        holder.orderDate.setText(model.getDate());
        holder.orderNums.setText(model.getOrderNums());
        holder.orderPrice.setText(model.getOrderPrice());
        holder.orderProducts.setText(model.getOrderProducts());

        if(model.getOrderCheck().equalsIgnoreCase("false")){
            holder.OrderCheck.setText("Order: Pending");
            holder.ScanQrCode.setVisibility(View.VISIBLE);
        }
        else{
            holder.OrderCheck.setText("Order: Received");
            holder.ScanQrCode.setVisibility(View.GONE);
        }

        holder.ScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScanQRCodeActivity.class);
                intent.putExtra("OrderId",model.getOrderID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

}
