package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshoppingsystem.Model.AdminOffer;
import com.example.groceryshoppingsystem.R;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

public class AdminOfferAdapter extends RecyclerView.Adapter<AdminOfferAdapter.ViewHolder> {
    private Context context;
    private List<AdminOffer> offers;
    private onItemClickListener itemListener;
    private onLongClickListener longListener;

    public interface onItemClickListener{
        void onItemClick(int pos);
    }
    public interface onLongClickListener{
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(onItemClickListener listener)
    {
        itemListener = listener;
    }

    public void setOnLongClickListener(onLongClickListener listener)
    {
        longListener = listener;
    }


    public AdminOfferAdapter(Context context, List<AdminOffer> offers) {
        this.context = context;
        this.offers = offers;
    }

    public void addList(List<AdminOffer> list)
    {
        offers.clear();
        Collections.copy(offers , list);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_options_list , parent , false);
        return new ViewHolder(v , itemListener , longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context , R.anim.fade_scale_animation));
        holder.img.setAnimation(AnimationUtils.loadAnimation(context , R.anim.fade_transition_animation));
        Picasso.get().load(offers.get(position).getOfferImg()).centerCrop().fit().into(holder.img);
        holder.name.setText(offers.get(position).getOfferName());
        holder.description.setText(offers.get(position).getOfferDescription());
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name , description;
        CardView cardView;
        public ViewHolder(@NonNull View itemView , final onItemClickListener itemlistener , final onLongClickListener longClickListener) {
            super(itemView);
            img = itemView.findViewById(R.id.adapterOfferImage);
            name = itemView.findViewById(R.id.adapterOfferName);
            description = itemView.findViewById(R.id.adapterOfferDescription);
            cardView = itemView.findViewById(R.id.OfferCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemlistener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            itemlistener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(longClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            longClickListener.onItemLongClick(position);
                    }
                    return false;
                }
            });
        }
    }
}