package com.example.cucutaae.mobileordering10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cucutaae.mobileordering10.order.Order;
import com.example.cucutaae.mobileordering10.order.OrderActivity;
import com.example.cucutaae.mobileordering10.order.OrderListAdapter;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestPaymentActivity extends AppCompatActivity {

    private TextView tvPay;
    private ImageButton ibCash;
    private ImageButton ibCard;
    private TextView tvOrderTableName;

    private OrderListAdapter adapter;

    private List<OrderProduct> mOrderProductListNew = new ArrayList<>();
    private List<OrderProduct> mOrderProductList = new ArrayList<>();

    private BigDecimal totalBill = BigDecimal.ZERO;
    private BigDecimal totalBillForUser = BigDecimal.ZERO;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refOrders = database.getReference("Orders");

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_payment);

        tvPay = (TextView) findViewById(R.id.tvPay);
        ibCash = (ImageButton) findViewById(R.id.ibCash);
        ibCard = (ImageButton) findViewById(R.id.ibCard);
        tvOrderTableName = (TextView) findViewById(R.id.tvOrderTableName);

        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = firebaseAuth.getCurrentUser();

        if(mFirebaseUser.getEmail() == null) {

            final String userName = Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName();

            setUserTable(userName);

        }else{

            setUserTable(mFirebaseUser.getEmail().split("@")[0]);
        }

        ibCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refOrders.orderByChild("state").equalTo(0).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Query query = refOrders.orderByChild("table").equalTo(tvOrderTableName.getText().toString());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshotOrderProd) {

                                for (DataSnapshot snapshot: dataSnapshotOrderProd.getChildren()) {
                                    snapshot.getRef().child("paymentType").setValue("cash");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        ibCard.setVisibility(View.GONE);
                        Toast.makeText(RequestPaymentActivity.this,"Requirement for payment sent ...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        ibCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refOrders.orderByChild("state").equalTo(0).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Query query = refOrders.orderByChild("table").equalTo(tvOrderTableName.getText().toString());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshotOrderProd) {

                                for (DataSnapshot snapshot: dataSnapshotOrderProd.getChildren()) {
                                    snapshot.getRef().child("paymentType").setValue("card");
                                }

                                ibCard.setVisibility(View.GONE);
                                Toast.makeText(RequestPaymentActivity.this,"Requirement for payment sent ...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void setUserTable(final String userNameLogIn) {

        refOrders.orderByChild("state").equalTo(0).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                String userTable = "noTable";

                Order order = dataSnapshot.getValue(Order.class);

                Log.v("ORDER_AVAILABLE", order.toString());

                Map<Object, Map<String, Object>> map = (Map<Object, Map<String, Object>>) dataSnapshot.getValue();

                for (Map.Entry<Object, Map<String, Object>> entry : map.entrySet())
                {
                    Log.v("DATA_V_MAP",entry.getKey().toString() + "/" + entry.getValue());

                    if(entry.getKey().toString().equalsIgnoreCase("userName")) {

                        if(entry.toString().split("=")[1].equals(userNameLogIn)){

                            userTable = order.getTable();
                            Log.v("USER_TABLE", userTable);

                            tvOrderTableName.setText(order.getTable());

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
