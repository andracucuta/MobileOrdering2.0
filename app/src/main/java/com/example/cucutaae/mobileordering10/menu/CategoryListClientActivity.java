package com.example.cucutaae.mobileordering10.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.adapter.MenuCategoryAdapter;
import com.example.cucutaae.mobileordering10.objects.MenuCategory;
import com.example.cucutaae.mobileordering10.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryListClientActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private List<MenuCategory> imgList;
    private ListView lv;
    private MenuCategoryAdapter adapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list_client);
        setTitle("MENU");

        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lviCategoryImageList);
        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FB_DATABASE_CATEGORIES_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MenuCategory img = snapshot.getValue(MenuCategory.class);
                    imgList.add(img);
                }

                //Init adapter
                adapter = new MenuCategoryAdapter(CategoryListClientActivity.this, R.layout.category_image_item, imgList);
                //Set adapter for listview
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick (AdapterView < ? > adapter, View view, int position, long arg){
                        TextView tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);

                        Intent intent = new Intent(getBaseContext(), ProductListActivity.class);

                        intent.putExtra("CATEGORY_TYPE", tvCategoryName.getText());

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });
    }
}
