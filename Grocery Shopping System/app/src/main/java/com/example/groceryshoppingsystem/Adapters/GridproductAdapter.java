package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.groceryshoppingsystem.Model.HorizontalProductModel;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.R;
import com.example.groceryshoppingsystem.UI.ProductInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.List;

public class GridproductAdapter extends BaseAdapter {
    List<HorizontalProductModel> horizontalProductModelList;
    ImageView productImage;
    TextView producttitle, productprice;
    ImageView checkBox;
    List<favouritesClass> favourites;
    ConstraintLayout Container;
    Context context;

    public GridproductAdapter(List<HorizontalProductModel> horizontalProductModelList, List<favouritesClass> favourites, Context context) {
        this.horizontalProductModelList = horizontalProductModelList;
        this.favourites = favourites;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, null);
            Container = view.findViewById(R.id.MainProductID);
            productImage = view.findViewById(R.id.item_image);
            producttitle = view.findViewById(R.id.item_title);
            productprice = view.findViewById(R.id.item_Price);
            checkBox = view.findViewById(R.id.check_box);
            Picasso.get().load(horizontalProductModelList.get(position).getProductimage()).into(productImage);
            producttitle.setText(horizontalProductModelList.get(position).getProducttitle());
            productprice.setText("EGP "+horizontalProductModelList.get(position).getProductprice());
            boolean isfavourite = false;
            for (int i = 0; i < favourites.size(); i++) {
                if (horizontalProductModelList.get(position).getProducttitle().equals(favourites.get(i).getProducttitle())) {
                    isfavourite = true;
                    horizontalProductModelList.get(position).setChecked(true);
                    break;
                }
            }
            if (isfavourite) {
                checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else
                checkBox.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
        } else {
            view = convertView;
        }
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favourites")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                HorizontalProductModel hz = horizontalProductModelList.get(position);
                if (!(horizontalProductModelList.get(position).isChecked())) {
                    horizontalProductModelList.get(position).setChecked(true);
                    checkBox = v.findViewById(R.id.check_box);
                    checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
                    ref.child(horizontalProductModelList.get(position).getProducttitle()).setValue(hz);
                } else {
                    horizontalProductModelList.get(position).setChecked(false);
                    checkBox = v.findViewById(R.id.check_box);
                    checkBox.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
                    ref.child(horizontalProductModelList.get(position).getProducttitle()).setValue(null);
                }
            }
        });

        Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductInfoActivity.class);
                intent.putExtra("Product Name",horizontalProductModelList.get(position).getProducttitle());
                intent.putExtra("Product Price",horizontalProductModelList.get(position).getProductprice());
                intent.putExtra("Product Image",horizontalProductModelList.get(position).getProductimage());
                intent.putExtra("Product ExpiryDate",horizontalProductModelList.get(position).getExpiredDate());
                intent.putExtra("Product IsFavorite",String.valueOf(horizontalProductModelList.get(position).isChecked()));
                intent.putExtra("Is Offered","no");

                context.startActivity(intent);
            }
        });

        return view;
    }

}