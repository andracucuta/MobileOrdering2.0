package com.example.cucutaae.mobileordering10.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.adapter.ReviewAdapter;
import com.example.cucutaae.mobileordering10.objects.MenuProduct;
import com.example.cucutaae.mobileordering10.objects.Order;
import com.example.cucutaae.mobileordering10.objects.OrderProduct;
import com.example.cucutaae.mobileordering10.objects.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductItemViewActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private ImageView ivProductItemImage;

    private TextView tvProductItemImage;
    private TextView tvPrice;
    private TextView tvCal;
    private TextView tvIngredientsText;
    private TextView tvDescriptionText;
    private EditText tvReviewToAdd;
    private RatingBar ratingBar;
    private Button btnAddReview;
    private ImageView ivAddProduct;

    private ListView lvReviewList;

    private ReviewAdapter adapter;
    private List<Review> mReviewList;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference refProducts = database.getReference("Products");
    DatabaseReference refOrders = database.getReference("Order");

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = firebaseAuth.getCurrentUser();

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

        ivAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyIfUserHasTable(mFirebaseUser.getEmail().split("@")[0]);

            }
        });
    }

    public void selectProductQuantity(final String user, final String orderKey)
    {
        final Dialog d = new Dialog(ProductItemViewActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_select_quantity);
        Button b1 = (Button) d.findViewById(R.id.btnSet);
        Button b2 = (Button) d.findViewById(R.id.btnCancel);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(20); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                OrderProduct orderProduct = new OrderProduct(Integer.parseInt(String.valueOf(np.getValue())),tvProductItemImage.getText().toString());

                refOrders.child(orderKey).child("OrderProducts").push().setValue(orderProduct);

                Toast.makeText(getApplicationContext(), "Added " + String.valueOf(np.getValue()) + " " + tvProductItemImage.getText(),Toast.LENGTH_LONG).show();

                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
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

         ivAddProduct = (ImageView)findViewById(R.id.ivAddProduct);
     }

    private void setProductImage( String tvProductName) {

        refProducts.orderByChild("name").equalTo(tvProductName).addChildEventListener(new ChildEventListener() {
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

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
    }


    private void verifyIfUserHasTable( String userName) {

        refOrders.orderByChild("user").equalTo(userName.trim()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Order order = dataSnapshot.getValue(Order.class);

                if(order.getState()==3){

                   Toast.makeText(getBaseContext(),"You can not place an order yet. " +
                           "You don't have a table.\n Please go to Enroll Table screen.", Toast.LENGTH_LONG).show();

                }else{
                    selectProductQuantity(mFirebaseUser.getEmail().split("@")[0],dataSnapshot.getKey());
                }

                Log.v("ORDER_V", dataSnapshot.getKey() + " " + order.toString());
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
