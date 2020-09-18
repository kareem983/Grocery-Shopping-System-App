package com.example.groceryshoppingsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class GridproductAdapter extends BaseAdapter {

    List<HorizontalProductModel> horizontalProductModelList;

    public GridproductAdapter(List<HorizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
     View view ;
   if (convertView == null){
view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item , null );
       ImageView productImage = view.findViewById(R.id.item_image);
       TextView producttitle = view.findViewById(R.id.item_title);
       TextView productdesc = view.findViewById(R.id.item_desc);
       TextView productprice = view.findViewById(R.id.item_price);
       productImage.setImageResource(horizontalProductModelList.get(position).getProductimage());
       producttitle.setText(horizontalProductModelList.get(position).getProducttitle());
       productdesc.setText(horizontalProductModelList.get(position).getProductdesc());
       productprice.setText(horizontalProductModelList.get(position).getProductprice());
   }
   else {
       view = convertView;
   }

        return view;
    }
}
