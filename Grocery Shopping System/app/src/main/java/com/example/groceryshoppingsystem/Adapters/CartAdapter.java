package com.example.groceryshoppingsystem.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshoppingsystem.Model.CartItemModel;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter  {
    private  List<CartItemModel> cartItemModelList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void UpdateTotalPrice(String str);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public CartAdapter(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType())
        {
            case 0:
                return  CartItemModel.cart_item;
            default:
                return  -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cartitemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);

        return new cartItemViewHolder(cartitemview , mListener);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String resource = cartItemModelList.get(position).getProductImage();
        String title = cartItemModelList.get(position).getProducttitle();
        int freeCoupons = cartItemModelList.get(position).getCoupon();
        int productPrice = cartItemModelList.get(position).getPrice();
        String cuttedprice = String.valueOf(cartItemModelList.get(position).getCuttedprice());
        int offerApplied = cartItemModelList.get(position).getOfferApplied();
        int quantity = cartItemModelList.get(position).getQuantity();
        ((cartItemViewHolder) holder).setItemDetails(resource, title, freeCoupons, productPrice , quantity, cuttedprice, offerApplied);

    }


    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }



    class cartItemViewHolder extends RecyclerView.ViewHolder  {
        private ImageView productimage;
        private TextView producttitle;
        private TextView freecoupon;
        private TextView productprice;
        private TextView cuttedprice;
        private TextView offerApplied;
        private TextView couponApplied;
        private TextView productQuantity;
        private ImageView couponIcon;
        private ImageView PlusIcon;
        private ImageView MinusIcon;
        private ImageView CartItemDelete;
        public boolean deletedItem = false ;
        //-----------



        //-------------
        int totalpriceVal;
        DatabaseReference root ;
        String CurrentUser;
        private FirebaseAuth mAuth;
        public cartItemViewHolder(@NonNull View itemView  ,final CartAdapter.OnItemClickListener listener) {
            super(itemView);
            productimage = itemView.findViewById(R.id.product_image);
            producttitle = itemView.findViewById(R.id.product_title);
            freecoupon = itemView.findViewById(R.id.coupon_txt);
            productprice = itemView.findViewById(R.id.price);
            cuttedprice = itemView.findViewById(R.id.cut_price);
            offerApplied = itemView.findViewById(R.id.offertxt);
            couponApplied = itemView.findViewById(R.id.couponApplied);
            productQuantity = itemView.findViewById(R.id.quan);
            couponIcon= itemView.findViewById(R.id.coupon);
            PlusIcon= itemView.findViewById(R.id.PlusIcon);
            MinusIcon= itemView.findViewById(R.id.MinusIcon);

            CartItemDelete= itemView.findViewById(R.id.Cart_ItemDelete);
            root = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            CurrentUser = mAuth.getCurrentUser().getUid();
            CartItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                            countTotalPrice();

                        }
                    }
                }
            });
        }



        void setItemDetails(String resource, final String title, int freeCouponsNo, final int productPriceText, int quantity, String cutprice, int offerAppliedNo){

            Picasso.get().load(resource).into(productimage);
            producttitle.setText(title);
            if (freeCouponsNo > 0) {
                couponIcon.setVisibility(View.VISIBLE);
                freecoupon.setVisibility(View.VISIBLE);
                couponApplied.setVisibility(View.VISIBLE);
                if (freeCouponsNo == 1) {
                    freecoupon.setText("free 1 coupon");
                } else {
                    freecoupon.setText("free" + freeCouponsNo + "coupons");
                }
            }
            else {
                couponIcon.setVisibility(View.INVISIBLE);
                freecoupon.setVisibility(View.INVISIBLE);
                couponApplied.setVisibility(View.INVISIBLE);
            }
            productprice.setText("Price: "+String.valueOf(productPriceText * quantity )+" EGP");
            if ( Integer.parseInt(cutprice) >0) {
                cuttedprice.setText(cutprice);
                cuttedprice.setVisibility(View.VISIBLE);
            }
            else {cuttedprice.setVisibility(View.INVISIBLE);}
            productQuantity.setText(String.valueOf(quantity));
            PlusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productQuantity.setText(String.valueOf( Integer.parseInt( productQuantity.getText().toString() )+1  )  );
                    productprice.setText("Price: "+String.valueOf(productPriceText *Integer.parseInt( productQuantity.getText().toString()) )+" EGP")  ;
                    root.child("cart").child(CurrentUser).child(title).child("quantity").setValue(productQuantity.getText().toString());
                    countTotalPrice();

                }
            });

            MinusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.valueOf( productQuantity.getText().toString() ) >1) {
                        productQuantity.setText(String.valueOf(Integer.parseInt(productQuantity.getText().toString()) - 1));
                        productprice.setText("Price: "+String.valueOf(productPriceText * Integer.parseInt(productQuantity.getText().toString()))+" EGP");
                        root.child("cart").child(CurrentUser).child(title).child("quantity").setValue(productQuantity.getText().toString());
                        countTotalPrice();
                    }
                }
            });



            if (offerAppliedNo>0)
            {
                offerApplied.setVisibility(View.VISIBLE);
                offerApplied.setText(offerAppliedNo + "offers Applied");
            }
            else{
                offerApplied.setVisibility(View.INVISIBLE);
            }



        }


    }
    class cartTotalAmountViewHolder extends RecyclerView.ViewHolder{
        private TextView totalItemsTitle;
        private TextView totalItemsPrice ;
        private TextView delivaryPrice;
        private TextView  totalAmount;
        private TextView savedAmount;

        public cartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItemsTitle= itemView.findViewById(R.id.Ttl_ItemsTitle);
            totalItemsPrice= itemView.findViewById(R.id.Ttl_ItemsPrice);
            delivaryPrice= itemView.findViewById(R.id.DelivaryPrice);
            totalAmount= itemView.findViewById(R.id.TotalAmount);

            savedAmount= itemView.findViewById(R.id.SavedAmount);
        }

        private void setTotalAmount(String totalItemsText,String totalItemsPriceText,String delivaryPriceText,String totalAmountText,String savedAmountText )
        {
            totalItemsTitle.setText(totalItemsText);
            totalItemsPrice.setText(totalItemsPriceText);
            delivaryPrice.setText(delivaryPriceText);
            totalAmount.setText(totalAmountText);
            savedAmount.setText(savedAmountText);
        }

    }
    public void countTotalPrice(){
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference(); ;

        DatabaseReference m = root.child("cart");

        final String CurrentUser =  FirebaseAuth.getInstance().getCurrentUser().getUid();

        ValueEventListener valueEventListener =new ValueEventListener() {
            int totalpriceVal = 0 ;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.child(CurrentUser).getChildren()) {
                        if (!dataSnapshot.getKey().equals("totalPrice")) {

                            String cartItemPrice = dataSnapshot.child("productPrice").getValue(String.class).toString();
                            String quantity = dataSnapshot.child("quantity").getValue(String.class).toString();
                            totalpriceVal += Integer.parseInt(  cartItemPrice) * Integer.parseInt( quantity );
                        }

                    }
                    root.child("cart").child(CurrentUser).child("totalPrice").setValue(String.valueOf(totalpriceVal));
                    mListener.UpdateTotalPrice(String.valueOf(totalpriceVal)+" EGP");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);

    }

}




