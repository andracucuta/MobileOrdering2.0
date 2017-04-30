package com.example.cucutaae.mobileordering10.menu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.adapter.ReviewAdapter;
import com.example.cucutaae.mobileordering10.objects.MenuProduct;
import com.example.cucutaae.mobileordering10.objects.Review;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductItemViewActivity extends AppCompatActivity {

    private ImageView ivProductItemImage;

    private TextView tvProductItemImage;
    private TextView tvPrice;
    private TextView tvCal;
    private TextView tvIngredientsText;
    private TextView tvDescriptionText;

    private EditText tvReviewToAdd;
    private RatingBar ratingBar;
    private Button btnAddReview;

    private ListView lvReviewList;

    private ReviewAdapter adapter;
    private List<Review> mReviewList;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Products");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        MenuProduct item = (MenuProduct) getIntent().getSerializableExtra("Product");

        setContent(item);


        Review review1 = new Review("cucuta.andra", "", new Date(), "The best latte in town.", 1.2f);
        Review review2 = new Review("cocos.daniel", "", new Date(), "I hate it...",2.5f);
        Review review3 = new Review("cucuta.andra", "", new Date(), "The best latte in town.", 3.0f);
        Review review4 = new Review("cocos.daniel", "", new Date(), "I hate it...",4.9f);

        mReviewList.add(review1);
        mReviewList.add(review2);
        mReviewList.add(review3);
        mReviewList.add(review4);

        adapter = new ReviewAdapter(getApplicationContext(), mReviewList);

        lvReviewList.setAdapter(adapter);

        lvReviewList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Review reviewNew = new Review("cucuta.andra" , "" , new Date() , tvReviewToAdd.getText().toString(), ratingBar.getRating());

                mReviewList.add(reviewNew);

                adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void setContent(MenuProduct item) {
        tvProductItemImage.setText(item.getName());

        setProductImage(item.getName());

        tvPrice.setText(Float.toString(item.getPrice()));
        tvDescriptionText.setText(item.getDescription());
        tvCal.setText(Integer.toString(item.getCal()));
        tvIngredientsText.setText(item.getIngredients());

    }

     private void init() {
         ivProductItemImage = (ImageView) findViewById(R.id.ivProductItemImage);
         tvProductItemImage = (TextView) findViewById(R.id.tvProductItemImage);

         tvPrice = (TextView) findViewById(R.id.tvPrice);
         tvCal = (TextView) findViewById(R.id.tvCal);

         tvIngredientsText = (TextView) findViewById(R.id.tvIngredientsText);
         tvDescriptionText = (TextView) findViewById(R.id.tvDescriptionText);

         ratingBar = (RatingBar) findViewById(R.id.ratingBar);
         tvReviewToAdd = (EditText) findViewById(R.id.tvReviewToAdd);
         btnAddReview = (Button) findViewById(R.id.btnAddReview);
         lvReviewList = (ListView) findViewById(R.id.lvReviewList);

         mReviewList = new ArrayList<>();
     }

    private void setProductImage( String tvProductName) {


        ref.orderByChild("name").equalTo(tvProductName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                MenuProduct menuProduct = dataSnapshot.getValue(MenuProduct.class);

                Log.v("PRODUCT_NAME", dataSnapshot.getKey() + " " + menuProduct.toString());

                Picasso.with(getBaseContext()).load(menuProduct.getImageURL()).into(ivProductItemImage);

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
