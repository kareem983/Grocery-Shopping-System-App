package com.example.groceryshoppingsystem;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class OffersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    //my variables
    private RecyclerView OffersRecycler;
    private AdminOptionsAdapter adapter;
    private FloatingActionButton OffersFloatingActionButton;


    public OffersFragment() {
        // Required empty public constructor
    }

    public static OffersFragment newInstance(String param1, String param2) {
        OffersFragment fragment = new OffersFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_offers, container, false);

        OffersRecycler= (RecyclerView)view.findViewById(R.id.OffersRecycler);
        OffersFloatingActionButton= (FloatingActionButton)view.findViewById(R.id.OffersFloatingBtnId);

        final ArrayList<AdminOptions> OptionArrayList = new ArrayList<>();
        for(int i=1;i<=10;i++){
            OptionArrayList.add(new AdminOptions("Offer "+i,R.drawable.ic_baseline_add_24));
        }

        adapter = new AdminOptionsAdapter(getActivity(),OptionArrayList);
        OffersRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        OffersRecycler.setAdapter(adapter);

        //on clicking to adding button
        OffersFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here add button
                startActivity(new Intent(getActivity(),AddOffer.class));

            }
        });

        return view;
    }
}