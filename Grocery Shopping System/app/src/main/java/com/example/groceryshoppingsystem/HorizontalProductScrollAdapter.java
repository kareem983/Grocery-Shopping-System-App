package com.example.groceryshoppingsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {
    private Context context;
    private List<HorizontalProductModel> modelList;

    public HorizontalProductScrollAdapter(Context context, List<HorizontalProductModel> modelList) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String res = modelList.get(position).getProductimage();
        String title = modelList.get(position).getProducttitle();
        String price = modelList.get(position).getProductprice();
        Picasso.get().load(modelList.get(position).getProductimage()).into((Target) holder);
        holder.setProducttitle(title);
        holder.setProductprice(price);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private ImageView productimage;
        private TextView producttitle;
        private TextView productprice;
        private ImageView checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productimage = itemView.findViewById(R.id.item_image);
            producttitle = itemView.findViewById(R.id.item_title);
            productprice = itemView.findViewById(R.id.item_Price);
            checkBox = itemView.findViewById(R.id.check_box);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favourites")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            HorizontalProductModel hz = modelList.get(pos);
            ref.child(modelList.get(pos).getProducttitle()).setValue(hz);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        }


        public void setProductimage(int res) {
            productimage.setImageResource(res);
        }


        public void setProducttitle(String title) {
            producttitle.setText(title);
        }

        public void setProductprice(String price) {
            productprice.setText(price);
        }
    }
}