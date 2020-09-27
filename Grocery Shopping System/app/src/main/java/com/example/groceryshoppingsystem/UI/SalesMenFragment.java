package com.example.groceryshoppingsystem.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.groceryshoppingsystem.Adapters.AdminOfferAdapter;
import com.example.groceryshoppingsystem.Adapters.AdminSalesManAdapter;
import com.example.groceryshoppingsystem.Model.AdminSalesMan;
import com.example.groceryshoppingsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class SalesMenFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    //my variables
    private RecyclerView SalesMenRecycler;
    private AdminSalesManAdapter adapter;
    private FloatingActionButton SalesFloatingActionButton;
    private List<AdminSalesMan> adminSalesManList;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;

    public SalesMenFragment() {
        // Required empty public constructor
    }

    public static SalesMenFragment newInstance(String param1, String param2) {
        SalesMenFragment fragment = new SalesMenFragment();
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
        view=inflater.inflate(R.layout.fragment_sales_men, container, false);

        SalesMenRecycler= (RecyclerView)view.findViewById(R.id.SalesMenRecycler);
        SalesFloatingActionButton= (FloatingActionButton)view.findViewById(R.id.SalesFloatingBtnId);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("salesman");
        bar = view.findViewById(R.id.salesManProgressBar);

        adminSalesManList = new ArrayList<>();

        bar.setVisibility(View.VISIBLE);
        adapter = new AdminSalesManAdapter(getActivity(),adminSalesManList);
        SalesMenRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SalesMenRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminSalesManList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    adminSalesManList.add(new AdminSalesMan(snapshot1.getKey() ,
                            snapshot1.child("img").getValue(String.class) ,
                            snapshot1.child("qrimage").getValue(String.class),
                            snapshot1.child("salary").getValue(String.class)));
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.setOnItemClickListener(new AdminOfferAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(getActivity() , EditSalesMan.class);
                Bundle b = new Bundle();
                b.putString("img" , adminSalesManList.get(pos).getImg());
                b.putString("name" , adminSalesManList.get(pos).getName());
                b.putString("salary" , adminSalesManList.get(pos).getSalary());
                b.putString("qrimg" , adminSalesManList.get(pos).getQrimg());
                i.putExtras(b);
                startActivity(i);
            }
        });

        adapter.setOnLongClickListener(new AdminOfferAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(final int pos) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("Confirmation").setMessage("Are You Sure You Want To Delete ?!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = mDataBaseRef.child(adminSalesManList.get(pos).getName());
                        reference.removeValue();
                        StorageReference a = FirebaseStorage.getInstance().getReference("salesman").child(adminSalesManList.get(pos).getName() + ".jpg");
                        a.delete();
                        StorageReference b = FirebaseStorage.getInstance().getReference("salesman").child(adminSalesManList.get(pos).getName());
                        b.delete();
                        StorageReference c = FirebaseStorage.getInstance().getReference("salesman").child(adminSalesManList.get(pos).getName() + "qr.jpg");
                        c.delete();
                        StorageReference d = FirebaseStorage.getInstance().getReference("salesman").child(adminSalesManList.get(pos).getName() + "qr");
                        d.delete();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();


            }
        });

        //on clicking to adding button
        SalesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here add button
                startActivity(new Intent(getActivity(),AddSalesMan.class));

            }
        });

        return view;
    }
}