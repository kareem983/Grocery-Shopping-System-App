package com.example.groceryshoppingsystem.UI;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.groceryshoppingsystem.Adapters.OrderAdapter;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class OrderFregmant extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ArrayList<MyorderModel> orderItemList;
    OrderAdapter adapter ;
    private FirebaseAuth mAuth;
    private String CurrentUser;
    private DatabaseReference m , root;
    public OrderFregmant() {
        // Required empty public constructor
    }
    private RecyclerView OrderItemRecyclerView;

    public static OrderFregmant newInstance(String param1, String param2) {
        OrderFregmant fragment = new OrderFregmant();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth=FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order_fregmant, container, false);
        OrderItemRecyclerView =  view.findViewById(R.id.orderrecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        OrderItemRecyclerView.setLayoutManager(layoutManager);
        orderItemList = new ArrayList<MyorderModel>();

        root = FirebaseDatabase.getInstance().getReference();
        m = root.child("order").child(CurrentUser);
        adapter = new OrderAdapter(orderItemList);

        OrderItemRecyclerView.setAdapter(adapter);
        ValueEventListener valueEventListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String Date = dataSnapshot.child("Date").getValue().toString();
                        int nums = ((int)(dataSnapshot.child("orderproducts").getChildrenCount()));
                        String totalPrice = dataSnapshot.child("totalPrice").getValue().toString();
                        String products="Products :\n";
                        for (DataSnapshot data : dataSnapshot.child("orderproducts").getChildren())
                        {
                            products+= "    #"+data.getKey() + "\n        Price: " + data.child("productPrice").getValue().toString() + " EGP\n        Quantity: " + data.child("quantity").getValue().toString()+"\n";
                        }
                        orderItemList.add( new MyorderModel("   Date :  " + Date ,"   Products Number :  "+String.valueOf(nums),"   Total Price :  "+ totalPrice , "   "+products));
                    }

                }
                else{
                    orderItemList.clear();
                }

                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);




        return view;
    }
}