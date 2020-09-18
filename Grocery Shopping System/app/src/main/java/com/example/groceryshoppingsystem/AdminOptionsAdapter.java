package com.example.groceryshoppingsystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminOptionsAdapter extends RecyclerView.Adapter<AdminOptionsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //define xml components
         ImageView OptionIcon;
         TextView OptionName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            OptionIcon = (ImageView)itemView.findViewById(R.id.OptionIcon);
            OptionName = (TextView)itemView.findViewById(R.id.OptionName);
        }
    }


    private Context context;
    private List<AdminOptions>AdminOptionsList;
    public AdminOptionsAdapter(Activity context, List<AdminOptions> AdminOptionsList) {
        this.context=context;
        this.AdminOptionsList=AdminOptionsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(context).inflate(R.layout.admin_options_list,parent,false);
        return new ViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminOptions adminOptions = AdminOptionsList.get(position);
        holder.OptionIcon.setImageResource(adminOptions.getOptionResourceId());
        holder.OptionName.setText(adminOptions.getOptionName());
    }

    @Override
    public int getItemCount() {
        return AdminOptionsList.size();
    }





}
