package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import com.example.groceryshoppingsystem.Model.model;
import com.example.groceryshoppingsystem.R;
import com.example.groceryshoppingsystem.UI.ProductInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class My_Adapter extends PagerAdapter {
    private List<model> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private String ProductName,ProductPrice,ProductImage,ProductNExpiryDate,ProductIsFavorite;

    public My_Adapter(List<model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView;
        TextView title, desc;
        CardView OfferCardContainer;

        OfferCardContainer = view.findViewById(R.id.OfferCardContainer);
        imageView = view.findViewById(R.id.contentImage);
        title = view.findViewById(R.id.contenttitle);
        desc = view.findViewById(R.id.contenDesc);
        Picasso.get().load(models.get(position).getImage()).into(imageView);
        title.setText(models.get(position).getTitle()+" Offer");
        desc.setText(models.get(position).getDesc());
        container.addView(view);


        OfferCardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductIsFavorite="false";
                getData(models.get(position).getTitle());

            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }






    private void getData(final String ProductNamee){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("product");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.child("Fruits").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductNamee)){
                            ProductName = ProductNamee;
                            ProductPrice=dataSnapshot.child("price").getValue().toString();
                            ProductImage=dataSnapshot.child("image").getValue().toString();
                            ProductNExpiryDate= dataSnapshot.child("expired").getValue().toString();
                            break;}
                    }
                    for(DataSnapshot dataSnapshot : snapshot.child("Electronics").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductNamee)){
                            ProductName = ProductNamee;
                            ProductPrice=dataSnapshot.child("price").getValue().toString();
                            ProductImage=dataSnapshot.child("image").getValue().toString();
                            ProductNExpiryDate= dataSnapshot.child("expired").getValue().toString();
                            break;}
                    }

                    for(DataSnapshot dataSnapshot : snapshot.child("Meats").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductNamee)){
                            ProductName = ProductNamee;
                            ProductPrice=dataSnapshot.child("price").getValue().toString();
                            ProductImage=dataSnapshot.child("image").getValue().toString();
                            ProductNExpiryDate= dataSnapshot.child("expired").getValue().toString();
                            break;}
                    }

                    for(DataSnapshot dataSnapshot : snapshot.child("Vegetables").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductNamee)){
                            ProductName = ProductNamee;
                            ProductPrice=dataSnapshot.child("price").getValue().toString();
                            ProductImage=dataSnapshot.child("image").getValue().toString();
                            ProductNExpiryDate= dataSnapshot.child("expired").getValue().toString();
                            break;}
                    }
                        getIsFav(ProductNamee);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getIsFav(final String ProductNamee){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UserId = mAuth.getCurrentUser().getUid();

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("favourites").child(UserId);
        ValueEventListener valueEventListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(ProductNamee)){
                        ProductIsFavorite ="true"; break;
                    }
                    else ProductIsFavorite = "false";
                }
                GoToProduct();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    private void GoToProduct(){
        Intent intent = new Intent(context, ProductInfoActivity.class);
        intent.putExtra("Product Name",ProductName);
        intent.putExtra("Product Price",ProductPrice);
        intent.putExtra("Product Image",ProductImage);
        intent.putExtra("Product ExpiryDate",ProductNExpiryDate);
        intent.putExtra("Product IsFavorite",ProductIsFavorite);
        intent.putExtra("Is Offered","yes");
        context.startActivity(intent);
    }
}