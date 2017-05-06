package com.example.cucutaae.mobileordering10.tableReservation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.dao.OrderDao;
import com.example.cucutaae.mobileordering10.menu.CategoryListClientActivity;
import com.example.cucutaae.mobileordering10.objects.MenuCategory;
import com.example.cucutaae.mobileordering10.objects.MenuProduct;
import com.example.cucutaae.mobileordering10.objects.Order;
import com.example.cucutaae.mobileordering10.order.OrderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableReservationActivity extends AppCompatActivity {

    private ImageView btnScan;
    private TextView twTableRegister;
    private TextView twTableRegisterRecomandation;
    private RelativeLayout layoutAfterTableRegister;
    private ImageView iwGoMenu;
    private ImageView iwGoOrder;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refOrders = database.getReference("Order");

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = firebaseAuth.getCurrentUser();

        verifyIfUserHasTable(mFirebaseUser.getEmail().split("@")[0]);

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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this,"You canceled the table scanning...", Toast.LENGTH_LONG).show();
            }else{

                layoutAfterTableRegister.setVisibility(View.VISIBLE);

                twTableRegister.setText(result.getContents());

                OrderDao orderDao = new OrderDao();

                orderDao.writeOrder(mFirebaseUser.getUid(), mFirebaseUser.getEmail().split("@")[0] , new Date(), result.getContents(), new ArrayList<MenuProduct>());

                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void verifyIfUserHasTable( String userName) {

        refOrders.orderByChild("user").equalTo(userName.trim()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Order order = dataSnapshot.getValue(Order.class);

                if(order.getState()!=3){

                    twTableRegister.setText(order.getTable());
                    layoutAfterTableRegister.setVisibility(View.VISIBLE);

                }else{

                    twTableRegister.setText("no table.");
                    layoutAfterTableRegister.setVisibility(View.INVISIBLE);

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
