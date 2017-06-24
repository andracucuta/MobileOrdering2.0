package com.example.cucutaae.mobileordering10.menu;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.utils.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class ProductListActivity extends AppCompatActivity {

    private TextView tvCategoryName;
    private ImageView ivProductPicture;
    private ListView lviProductsImageList;
    private ImageView ivbtnBack;
    private TextView tvCategName;

    private DatabaseReference mDatabaseRef;
    private List<MenuProduct> productList;
    private ListView lv;
    private MenuProductAdapter adapter;
    private ProgressDialog progressDialog;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Categories");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        final String tvCategoryName =  getIntent().getStringExtra("CATEGORY_TYPE");

        initComponents();

        tvCategName.setText(tvCategoryName);

        setCategoryImage(tvCategoryName);

        ivbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductListActivity.this,CategoryListClientActivity.class));
            }
        });

        productList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lviProductsImageList);
        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading list image...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FB_DATABASE_PRODUCTS_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MenuProduct img = snapshot.getValue(MenuProduct.class);
                    if(img.getCategory().equalsIgnoreCase(tvCategoryName)) {
                        img.setProductId(snapshot.getKey());
                        productList.add(img);
                    }
                }

                //Init adapter
                adapter = new MenuProductAdapter(ProductListActivity.this, R.layout.product_image_item, productList);
                //Set adapter for listview
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick (AdapterView < ? > adapter, View view, int position, long arg){

                        Intent intent = new Intent(getBaseContext(), ProductItemViewActivity.class);

                        MenuProduct item = (MenuProduct) lv.getItemAtPosition(position);

                        Log.v("PRODUCT_SENT: "," " + item.toString());

                        intent.putExtra("Product",item);

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


    public void initComponents(){

        tvCategName = (TextView) findViewById(R.id.tvCategName);

        ivbtnBack = (ImageView) findViewById(R.id.ivbtnBack);

        ivProductPicture = (ImageView) findViewById(R.id.ivProductPicture);

        tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);

        lviProductsImageList = (ListView) findViewById(R.id.lviProductsImageList);

    }

    private void setCategoryImage( String tvCategoryName) {

        ref.orderByChild("name").equalTo(tvCategoryName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                MenuCategory menuCategory = dataSnapshot.getValue(MenuCategory.class);

                Picasso.with(getBaseContext()).load(menuCategory.getUrl()).into(ivProductPicture);

                Log.v("Name_V", dataSnapshot.getKey() + " " + menuCategory.toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}


