package com.example.groceryshoppingsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFregmant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFregmant extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFregmant.
     */
    // TODO: Rename and change types and number of parameters
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
        m = root.child("order");
        adapter = new OrderAdapter(orderItemList);

        OrderItemRecyclerView.setAdapter(adapter);
        ValueEventListener valueEventListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.child(CurrentUser).getChildren()) {

                     String Date = dataSnapshot.child("Date").getValue().toString();
                     int nums = ((int)(dataSnapshot.child("orderproducts").getChildrenCount()));
                         String totalPrice = dataSnapshot.child("totalPrice").getValue().toString();
                     String products="Products :\n";
                     for (DataSnapshot data : dataSnapshot.child("orderproducts").getChildren())
                     {
                         products+= "   "+data.getKey() + "   " + data.child("productPrice").getValue().toString() + "   " + data.child("quantity").getValue().toString()+"\n";
                     }
                     orderItemList.add( new MyorderModel("   Date :  " + Date ,"   Products Number :  "+String.valueOf(nums),"   Total Price :  "+ totalPrice , "   "+products));
                    }

                    adapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);




        return view;
    }
}