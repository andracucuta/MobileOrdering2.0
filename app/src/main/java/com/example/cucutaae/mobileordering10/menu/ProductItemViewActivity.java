package com.example.cucutaae.mobileordering10.menu;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.cucutaae.mobileordering10.order.Order;
import com.example.cucutaae.mobileordering10.order.OrderProduct;
import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductItemViewActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private ImageView ivProductItemImage;
    private TextView tvProductItemImage;
    private TextView tvPrice;
    private TextView tvCal;
    private TextView tvIngredientsText;
    private TextView tvDescriptionText;
    private TextView tvOrderTableName;
    private EditText tvReviewToAdd;
    private RatingBar ratingBar;
    private Button btnAddReview;
    private FloatingActionButton ivAddProduct;
    private TextView tvReviewListEmpty;
    private ListView lvReviewList;

    private ReviewAdapter adapter;
    private List<Review> mReviewList;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference refReviews = database.getReference("Reviews");
    DatabaseReference refProducts = database.getReference("Products");
    DatabaseReference refOrders = database.getReference("Orders");

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

        initComponents();

        final MenuProduct item = (MenuProduct) getIntent().getSerializableExtra("Product");

        setContent(item);

        if(mFirebaseUser.getEmail() == null){
            Profile profile = Profile.getCurrentProfile();

            setUserTable(profile.getFirstName() + " " + profile.getLastName());

        }else {
            setUserTable(mFirebaseUser.getEmail().split("@")[0]);

        }

        refReviews.child(item.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mReviewList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Review review = snapshot.getValue(Review.class);
                    mReviewList.add(review);

                    Log.v("PRODUCT_REVIEW", review.toString());

                }

                adapter = new ReviewAdapter(getApplicationContext(), mReviewList);

                lvReviewList.setAdapter(adapter);

                if (!mReviewList.isEmpty()) {

                    tvReviewListEmpty.setVisibility(View.GONE);

                }else{
                    lvReviewList.setVisibility(View.GONE);
                    tvReviewListEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date today = Calendar.getInstance().getTime();
                String reviewDate = df.format(today);

                Review reviewNew;

                if(mFirebaseUser.getEmail() == null){
                    Profile profile = Profile.getCurrentProfile();

                    Log.v("FACEBOOK_USER: ", "Logged, user name=" + profile.getFirstName() + " " + profile.getLastName());

                    reviewNew = new Review(profile.getFirstName() + " " + profile.getLastName(), reviewDate,
                            tvReviewToAdd.getText().toString(), ratingBar.getRating());
                }else {
                    reviewNew = new Review(mFirebaseUser.getEmail().split("@")[0], reviewDate,
                            tvReviewToAdd.getText().toString(), ratingBar.getRating());
                }

                lvReviewList.setVisibility(View.VISIBLE);
                tvReviewListEmpty.setVisibility(View.GONE);

                mReviewList.add(reviewNew);

                adapter.notifyDataSetChanged();

                refReviews.child(item.getName()).push().setValue(reviewNew);

                Toast.makeText(getApplicationContext(), "Review added ",Toast.LENGTH_LONG).show();

                tvReviewToAdd.setText("");
                ratingBar.setRating(0.0f);

            }
        });



        ivAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mFirebaseUser.getEmail() == null){
                    Profile profile = Profile.getCurrentProfile();

                    Log.v("FACEBOOK_USER: ", "Logged, user name=" + profile.getFirstName() + " " + profile.getLastName());

                    verifyIfUserHasTable(profile.getFirstName() + " " +  profile.getLastName());
                }else {
                    verifyIfUserHasTable(mFirebaseUser.getEmail().split("@")[0]);
                }

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

                firebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = firebaseAuth.getCurrentUser();

                OrderProduct orderProduct;

                if(mFirebaseUser.getEmail() == null){
                    Profile profile = Profile.getCurrentProfile();

                    Log.v("FACEBOOK_USER: ", "Logged, user name=" + profile.getFirstName() + " " + profile.getLastName());

                    verifyIfUserHasTable(profile.getFirstName() + " " +  profile.getLastName());
                    orderProduct = new OrderProduct(Integer.parseInt(String.valueOf(np.getValue()))
                            ,tvProductItemImage.getText().toString(), tvPrice.getText().toString(),
                            profile.getFirstName() + " " +  profile.getLastName(), tvOrderTableName.getText().toString());
                }else {
                    orderProduct = new OrderProduct(Integer.parseInt(String.valueOf(np.getValue()))
                            ,tvProductItemImage.getText().toString(), tvPrice.getText().toString(),
                            mFirebaseUser.getEmail().split("@")[0], tvOrderTableName.getText().toString());
                }

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

       // setReviewList(item.getName());

    }

     private void initComponents() {
         ivProductItemImage = (ImageView) findViewById(R.id.ivProductItemImage);
         tvProductItemImage = (TextView) findViewById(R.id.tvProductItemImage);

         tvPrice = (TextView) findViewById(R.id.tvPrice);
         tvCal = (TextView) findViewById(R.id.tvCal);

         tvIngredientsText = (TextView) findViewById(R.id.tvIngredientsText);
         tvDescriptionText = (TextView) findViewById(R.id.tvDescriptionText);
         tvOrderTableName = (TextView) findViewById(R.id.tvOrderTableName);

         ratingBar = (RatingBar) findViewById(R.id.ratingBar);
         tvReviewToAdd = (EditText) findViewById(R.id.tvReviewToAdd);
         btnAddReview = (Button) findViewById(R.id.btnAddReview);
         lvReviewList = (ListView) findViewById(R.id.lvReviewList);

         mReviewList = new ArrayList<>();

         ivAddProduct = (FloatingActionButton) findViewById(R.id.ivAddProduct);

         tvReviewListEmpty = (TextView) findViewById(R.id.tvReviewListEmpty);

     }

     private void setReviewList(final String tvProductName){

         refReviews.child(tvProductName).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                     Review review = snapshot.getValue(Review.class);
                         mReviewList.add(review);

                         Log.v("PRODUCT_REVIEW", snapshot.getKey()+"+");
                         Log.v("PRODUCT_REVIEW", tvProductName+"+");
                         Log.v("PRODUCT_REVIEW", review.toString());
                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
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


    private void verifyIfUserHasTable(final String userNameLogIn) {

        refOrders.orderByChild("state").equalTo(0).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                boolean userHasTable = false;

                Order order = dataSnapshot.getValue(Order.class);

                Log.v("ORDER_AVAILABLE", order.toString());

                Map<Object, Map<String, Object>> map = (Map<Object, Map<String, Object>>) dataSnapshot.getValue();

                for (Map.Entry<Object, Map<String, Object>> entry : map.entrySet()) {

                    Log.v("DATA_V_MAP", entry.getKey().toString() + "/" + entry.getValue());

                    if (entry.getKey().toString().equalsIgnoreCase("userName")) {

                        Log.v("USERNAEMLOGIN", userNameLogIn);

                        if (entry.toString().split("=")[1].equals(userNameLogIn)) {

                            userHasTable = true;

                        }
                    }
                }

                if (userHasTable == true) {

                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.getEmail() == null) {

                        Profile profile = Profile.getCurrentProfile();

                        Log.v("FACEBOOK_USER: ", "Logged, user name=" + profile.getFirstName() + " " + profile.getLastName());

                        selectProductQuantity(profile.getFirstName() + " " + profile.getLastName(), dataSnapshot.getKey());
                    } else {
                        selectProductQuantity(mFirebaseUser.getEmail().split("@")[0], dataSnapshot.getKey());
                    }
                }
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

    private void setUserTable(final String userNameLogIn) {

        refOrders.orderByChild("state").equalTo(0).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Query query = refOrders.orderByChild("userName").equalTo(userNameLogIn).limitToFirst(1);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshotOrderProd) {

                        String userTable = "noTable";

                        if (dataSnapshotOrderProd.getValue() != null) {

                            String [] orderElem = dataSnapshotOrderProd.getValue().toString().replace("{", "").split(",");

                            for(String s : orderElem){
                                String [] elem = s.split("=");

                                if("table".equalsIgnoreCase(elem[0].trim())){
                                    userTable = elem[1];
                                    break;
                                }
                            }

                            Log.v("USER_TABLE", userTable);

                            tvOrderTableName.setText(userTable);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
