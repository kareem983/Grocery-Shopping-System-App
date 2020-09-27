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
import android.widget.Toast;
import com.example.groceryshoppingsystem.Model.AdminOffer;
import com.example.groceryshoppingsystem.Adapters.AdminOfferAdapter;
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

public class OffersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    //my variables
    private RecyclerView OffersRecycler;
    private AdminOfferAdapter adapter;
    private FloatingActionButton OffersFloatingActionButton;
    private List<AdminOffer> OfferList;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;


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

        bar = view.findViewById(R.id.offerProgressBar);
        OffersRecycler= (RecyclerView)view.findViewById(R.id.OffersRecycler);
        OffersFloatingActionButton= (FloatingActionButton)view.findViewById(R.id.OffersFloatingBtnId);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("offers");
        OfferList = new ArrayList<>();

        adapter = new AdminOfferAdapter(getActivity(),OfferList);
        OffersRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        OffersRecycler.setAdapter(adapter);
        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OfferList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    OfferList.add(new AdminOffer(snapshot1.getKey() ,
                            snapshot1.child("describtion").getValue(String.class) ,
                            snapshot1.child("img").getValue(String.class)));
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                bar.setVisibility(View.INVISIBLE);
            }
        });

        adapter.setOnItemClickListener(new AdminOfferAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(getActivity() , EditOffer.class);
                Bundle b = new Bundle();
                b.putString("img" , OfferList.get(pos).getOfferImg());
                b.putString("name" , OfferList.get(pos).getOfferName());
                b.putString("describtion" , OfferList.get(pos).getOfferDescription());
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
                        DatabaseReference reference = mDataBaseRef.child(OfferList.get(pos).getOfferName());
                        reference.removeValue();
                        StorageReference z = FirebaseStorage.getInstance().getReference("offers").child(OfferList.get(pos).getOfferName() + ".jpg");
                        z.delete();
                        StorageReference x = FirebaseStorage.getInstance().getReference("offers").child(OfferList.get(pos).getOfferName());
                        x.delete();
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