package com.example.groceryshoppingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private GridView gridView;
    private Toolbar mToolBar;
    private AdminOptionsAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private String Option1;
    private String Option2;
    private String Option3;
    private String Option4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mToolBar = (Toolbar)findViewById(R.id.Admin_ToolBar);
        setSupportActionBar(mToolBar);

        gridView= (GridView)findViewById(R.id.AdminGridOptions);
        bottomNavigationView= (BottomNavigationView)findViewById(R.id.Bottom_view);

        Option1= "Add new Salesman";
        Option2= "Set new Offer";
        Option3= "Delete Salesman";
        Option4= "set Discount";

        final ArrayList<AdminOptions> OptionArrayList = new ArrayList<>();
        OptionArrayList.add(new AdminOptions(Option1,R.drawable.ic_baseline_add_24));
        OptionArrayList.add(new AdminOptions(Option2,R.drawable.ic_baseline_local_offer_24));
        OptionArrayList.add(new AdminOptions(Option3,R.drawable.ic_baseline_delete_24));
        OptionArrayList.add(new AdminOptions(Option4,R.drawable.ic_baseline_edit_24));


        adapter = new AdminOptionsAdapter(this,OptionArrayList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AdminOptions adminOptions = OptionArrayList.get(i);

                if(adminOptions.getOptionName().equals(Option1)){

                }
                else if(adminOptions.getOptionName().equals(Option2)){

                }
                else if(adminOptions.getOptionName().equals(Option3)){

                }
                else if(adminOptions.getOptionName().equals(Option4)){

                }

            }
        });


    }


    private BottomNavigationView.OnNavigationItemSelectedListener naveLis=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if(id==R.id.HomeID){

                    }
                    else if(id==R.id.CreateID){

                    }
                    else if(id==R.id.AddID){

                    }
                    else if(id==R.id.EditID){

                    }
                    else if(id==R.id.DeleteID){

                    }
                    return false;
                }
            };

}