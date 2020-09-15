package com.example.groceryshoppingsystem;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class ProductsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;


    //my variables
    private RecyclerView ProductsRecycler;
    private AdminOptionsAdapter adapter;


    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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
         view=inflater.inflate(R.layout.fragment_products, container, false);

        ProductsRecycler= (RecyclerView)view.findViewById(R.id.ProductsRecycler);

        final ArrayList<AdminOptions> OptionArrayList = new ArrayList<>();
        OptionArrayList.add(new AdminOptions("Apple",R.drawable.ic_baseline_add_24));
        OptionArrayList.add(new AdminOptions("Tomato",R.drawable.ic_baseline_local_offer_24));
        OptionArrayList.add(new AdminOptions("Banana",R.drawable.ic_baseline_delete_24));
        OptionArrayList.add(new AdminOptions("Grape",R.drawable.ic_baseline_edit_24));

        OptionArrayList.add(new AdminOptions("Cucumber",R.drawable.ic_baseline_add_24));
        OptionArrayList.add(new AdminOptions("pepper",R.drawable.ic_baseline_local_offer_24));
        OptionArrayList.add(new AdminOptions("plum",R.drawable.ic_baseline_delete_24));
        OptionArrayList.add(new AdminOptions("Date",R.drawable.ic_baseline_edit_24));

        OptionArrayList.add(new AdminOptions("coriander",R.drawable.ic_baseline_add_24));
        OptionArrayList.add(new AdminOptions("Orange",R.drawable.ic_baseline_local_offer_24));
        OptionArrayList.add(new AdminOptions("pomegranate",R.drawable.ic_baseline_delete_24));
        OptionArrayList.add(new AdminOptions("watermelon",R.drawable.ic_baseline_edit_24));

        adapter = new AdminOptionsAdapter(getActivity(),OptionArrayList);
        ProductsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProductsRecycler.setAdapter(adapter);



        return view;
    }


}