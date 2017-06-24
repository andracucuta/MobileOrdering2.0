package com.example.cucutaae.mobileordering10.tableReservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.aboutus.PlaceInfo;
import com.example.cucutaae.mobileordering10.order.Order;
import com.example.cucutaae.mobileordering10.order.OrderDao;
import com.example.cucutaae.mobileordering10.menu.CategoryListClientActivity;
import com.example.cucutaae.mobileordering10.order.OrderActivity;
import com.example.cucutaae.mobileordering10.signin.User;
import com.facebook.Profile;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Map;

public class TableReservationActivity extends AppCompatActivity {

    private ImageView btnScan;
    private TextView twTableRegister;
    private TextView twTableRegisterRecomandation;
    private RelativeLayout layoutAfterTableRegister;
    private ImageView iwGoMenu;
    private ImageView iwGoOrder;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference refOrders = database.getReference("Orders");
    DatabaseReference refUsers = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        Firebase.setAndroidContext(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user.getEmail() == null){
            Profile profile = Profile.getCurrentProfile();

            Log.v("FACEBOOK_USER: ", "Logged, user name=" + profile.getFirstName() + " " + profile.getLastName());

            verifyIfUserHasTable(profile.getFirstName() + " " +  profile.getLastName());
        }else {
            verifyIfUserHasTable(user.getEmail().split("@")[0]);
        }

        final Activity activity = this;

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("no table.".equalsIgnoreCase(twTableRegister.getText().toString())) {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Scan table");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);

                    //integrator.setBarcodeImageEnabled(false);

                    integrator.initiateScan();
                }else{

                    Toast.makeText(getBaseContext(),"You allready have a table. Talk to the waiter if you want to change it.", Toast.LENGTH_LONG).show();
                }
            }
        });

        iwGoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenu = new Intent(TableReservationActivity.this, CategoryListClientActivity.class);
                intentMenu.putExtra("USER_TYPE","client");
                TableReservationActivity.this.startActivity(intentMenu);
            }
        });

        iwGoOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenu = new Intent(TableReservationActivity.this, OrderActivity.class);
                TableReservationActivity.this.startActivity(intentMenu);
            }
        });
    }

    private void init() {
        btnScan = (ImageView)findViewById(R.id.btnScan);
        twTableRegister = (TextView)findViewById(R.id.twTableRegister);
        twTableRegisterRecomandation = (TextView)findViewById(R.id.twTableRegister);
        layoutAfterTableRegister = (RelativeLayout)findViewById(R.id.layoutAfterTableRegister);
        iwGoMenu = (ImageView)findViewById(R.id.iwGoMenu);
        iwGoOrder = (ImageView)findViewById(R.id.iwGoOrder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this,"You canceled the table scanning...", Toast.LENGTH_LONG).show();
            }else{

                layoutAfterTableRegister.setVisibility(View.VISIBLE);

                twTableRegister.setText(result.getContents());

                final OrderDao orderDao = new OrderDao();

                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user.getEmail() == null) {

                    final String userName = Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName();
                    orderDao.registerTable(userName, new Date(), result.getContents());

                }else{

                    orderDao.registerTable( user.getEmail().split("@")[0],
                            new Date(), result.getContents());
                }

                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void verifyIfUserHasTable(final String userNameLogIn) {

        twTableRegister.setText("no table.");
        layoutAfterTableRegister.setVisibility(View.INVISIBLE);

        refOrders.orderByChild("state").equalTo(0).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Order order = dataSnapshot.getValue(Order.class);

                Log.v("ORDER_AVAILABLE", order.toString());

                Map<Object, Map<String, Object>> map = (Map<Object, Map<String, Object>>) dataSnapshot.getValue();

                for (Map.Entry<Object, Map<String, Object>> entry : map.entrySet())
                {
                    Log.v("DATA_V_MAP",entry.getKey().toString() + "/" + entry.getValue());

                    if(entry.getKey().toString().equalsIgnoreCase("userName")) {

                        if(entry.toString().split("=")[1].equals(userNameLogIn)){

                            twTableRegister.setText(order.getTable());
                            layoutAfterTableRegister.setVisibility(View.VISIBLE);

                            break;
                        }
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
}


