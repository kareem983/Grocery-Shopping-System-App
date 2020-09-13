package com.example.groceryshoppingsystem;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminOptionsAdapter extends ArrayAdapter<AdminOptions> {

    public AdminOptionsAdapter(Activity context, ArrayList<AdminOptions> androidFlavors) {
        super(context, 0, androidFlavors);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ListItemView = convertView;

        if(ListItemView == null) {
            ListItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.admin_options_list, parent, false);
        }

        AdminOptions currentOption = getItem(position);

        //define xml components
        ImageView OptionIcon = (ImageView)ListItemView.findViewById(R.id.OptionIcon);
        TextView OptionName = (TextView)ListItemView.findViewById(R.id.OptionName);

        OptionIcon.setImageResource(currentOption.getOptionResourceId());
        OptionName.setText(currentOption.getOptionName());

        return ListItemView;
    }

}
