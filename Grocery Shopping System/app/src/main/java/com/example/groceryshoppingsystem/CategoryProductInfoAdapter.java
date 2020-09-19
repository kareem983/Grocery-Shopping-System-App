package com.example.groceryshoppingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CategoryProductInfoAdapter extends RecyclerView.Adapter<CategoryProductInfoAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ProductImage;
            private TextView ProductName;
            private TextView ProductPrice;
            private TextView ProductExpiryDate;
            private ImageView PrFavoriteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductImage = (ImageView)itemView.findViewById(R.id.PrImage);
            ProductName = (TextView)itemView.findViewById(R.id.PrName);
            ProductPrice = (TextView)itemView.findViewById(R.id.PrPrice);
            ProductExpiryDate = (TextView)itemView.findViewById(R.id.PrExpiryDate);
            PrFavoriteImage = (ImageView)itemView.findViewById(R.id.PrFavoriteImage);
        }
    }


    private Context context;
    private List<CategoryProductInfo> ProductList;

    public CategoryProductInfoAdapter(Context context, List<CategoryProductInfo> ProductList){
        this.context = context;
        this.ProductList = ProductList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_products_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryProductInfoAdapter.ViewHolder holder, int position) {
        final CategoryProductInfo product = ProductList.get(position);

        Picasso.get().load(product.getProductImage()).into(holder.ProductImage);
        holder.ProductName.setText(product.getProductName());
        holder.ProductPrice.setText(product.getProductPrice());
        holder.ProductExpiryDate.setText(product.getProductExpiryDate());

        if(product.getProductExpiryDate().equals("Expiry Date: null")) holder.ProductExpiryDate.setVisibility(View.INVISIBLE);
        else holder.ProductExpiryDate.setVisibility(View.VISIBLE);

        if(product.getIsFavorite()){
            holder.PrFavoriteImage.setImageResource(R.drawable.red_favorite);
        }
        else{
            holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24);
        }

        holder.PrFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.getIsFavorite()){
                    holder.PrFavoriteImage.setImageResource(R.drawable.red_favorite);
                    product.setFavorite(false);
                }
                else{
                    holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24);
                    product.setFavorite(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }

}
