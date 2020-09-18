package com.example.groceryshoppingsystem;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {
private List<HorizontalProductModel>modelList;

    public HorizontalProductScrollAdapter(List<HorizontalProductModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          int res = modelList.get(position).getProductimage();
          String title = modelList.get(position).getProducttitle();
          String desc = modelList.get(position).getProductdesc();
          String price = modelList.get(position).getProductprice();

          holder.setProductimage(res);
          holder.setProducttitle(title);
          holder.setProductdesc(desc);
          holder.setProductprice(price);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productimage;
        private TextView producttitle;
        private TextView productdesc;
        private TextView productprice ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productimage = itemView.findViewById(R.id.item_image);
            producttitle = itemView.findViewById(R.id.item_title);
            productdesc = itemView.findViewById(R.id.item_desc);
            productprice = itemView.findViewById(R.id.item_price);
        }


        public void setProductimage(int res) {
           productimage.setImageResource(res);
        }


        public void setProducttitle(String title) {
            producttitle.setText(title);
        }

        public void setProductdesc(String desc) {
           productdesc.setText(desc);
        }


        public void setProductprice(String price) {
            productprice.setText(price);
        }
    }
}
