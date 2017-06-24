package com.example.cucutaae.mobileordering10.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
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

public class OrderActivity extends AppCompatActivity {

    private ListView lvProductList;
    private TextView tvOrderTotal;
    private TextView tvOrderNoOrder;
    private TextView tvOrderTableName;
    private TextView tvOrderTotalAll;
    private int count = 0;
    private Button btnSubmitOrder;

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
        setContentView(R.layout.activity_order);

        init();

        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = firebaseAuth.getCurrentUser();

        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
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
                                    snapshot.getRef().child("confirmed").setValue("Yes");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(OrderActivity.this,"Order Confirmed ...", Toast.LENGTH_SHORT).show();
                        btnSubmitOrder.setVisibility(View.GONE);
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

                            populateProductListOrdered(tvOrderTableName.getText().toString(), userNameLogIn);

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

    private void populateProductListOrdered(final String tableName, final String userNameLogIn) {

            refOrders.orderByChild("state").equalTo(0).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                    if (dataSnapshot.getValue() != null) {

                        mOrderProductList = new ArrayList<>();
                        //mOrderProductListNew = new ArrayList<>();

                        Order order = dataSnapshot.getValue(Order.class);

                        Log.v("ORDER_AVAILABLE", order.toString());

                        Map<Object, Map<String, Object>> map = (Map<Object, Map<String, Object>>) dataSnapshot.getValue();

                        String key = dataSnapshot.getKey();

                        for (Map.Entry<Object, Map<String, Object>> entry : map.entrySet()) {
                            Log.v("DATA_V_MAP", entry.getKey().toString() + "/" + entry.getValue());

                            if (entry.getKey().toString().equalsIgnoreCase("OrderProducts")) {

                                for (Map.Entry<String, Object> entryObjects : entry.getValue().entrySet()) {

                                    String orderKey = entryObjects.getKey();

                                    Log.v("MAP_OrderProducts", entryObjects.getKey() + "/" + entryObjects.getValue());

                                    Object productObject = entryObjects.getValue();

                                    String productDetails1 = productObject.toString();
                                    productDetails1.replace("{", "");
                                    productDetails1.replace("}", "");

                                    String[] productDetails = productDetails1.split(",");
                                    OrderProduct orderProduct = new OrderProduct();
                                    orderProduct.setKey(key);
                                    orderProduct.setOrderKey(orderKey);
                                    int i = 0;
                                    for (String s : productDetails) {

                                        Log.v("OrderProducts-Split[" + i++ + "]: ", s);

                                        if (s.split("=")[0].replace("{", "").equals("quantity")) {
                                            orderProduct.setQuantity(Integer.parseInt(s.split("=")[1].trim()));
                                        } else if (s.split("=")[0].trim().equals("price")) {
                                            orderProduct.setPrice(s.split("=")[1].trim());
                                        } else if (s.split("=")[0].trim().equals("user")) {
                                            orderProduct.setUser(s.split("=")[1].trim());
                                        } else if (s.split("=")[0].trim().equals("table")) {
                                            orderProduct.setTable(s.split("=")[1].trim());
                                        } else if (s.split("=")[0].trim().equals("productName")) {
                                            orderProduct.setProductName(s.split("=")[1].trim().replace("}", ""));
                                        }
                                    }
                                    Log.v("Product Created: ", orderProduct.toString());
                                    mOrderProductList.add(orderProduct);
                                }
                                break;
                            }
                        }
                    }

                    if (!mOrderProductList.isEmpty()) {

                        for (OrderProduct orderProduct : mOrderProductList) {
                            if (tableName.equalsIgnoreCase(orderProduct.getTable())) {
                                mOrderProductListNew.add(orderProduct);
                            }
                        }

                        List<OrderProduct> userOrderProd = new ArrayList<>();

                        for (OrderProduct orderProduct : mOrderProductListNew) {
                            if (userNameLogIn.equalsIgnoreCase(orderProduct.getUser())) {
                                userOrderProd.add(orderProduct);
                            }
                        }

                        tvOrderNoOrder.setVisibility(View.GONE);

                        totalBillForUser = calculateTotalPrice(userOrderProd);
                        totalBillForUser = totalBillForUser.setScale(2, RoundingMode.CEILING);
                        tvOrderTotal.setText(String.valueOf(totalBillForUser) + "$");

                        totalBill = calculateTotalPrice(mOrderProductListNew);
                        totalBill = totalBill.setScale(2, RoundingMode.CEILING);
                        tvOrderTotalAll.setText(String.valueOf(totalBill) + "$");

                        lvProductList.setVisibility(View.VISIBLE);
                        btnSubmitOrder.setVisibility(View.VISIBLE);

                        adapter = new OrderListAdapter(getApplicationContext(), mOrderProductListNew);

                        lvProductList.setAdapter(adapter);

                    } else {
                        lvProductList.setVisibility(View.INVISIBLE);
                        btnSubmitOrder.setVisibility(View.INVISIBLE);
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

    private void init() {
        lvProductList = (ListView) findViewById(R.id.lvProductList);
        tvOrderTotal = (TextView) findViewById(R.id.tvOrderTotal);
        tvOrderNoOrder = (TextView) findViewById(R.id.tvOrderNoOrder);
        tvOrderTableName = (TextView) findViewById(R.id.tvOrderTableName);
        tvOrderTotalAll = (TextView) findViewById(R.id.tvOrderTotalAll);

        btnSubmitOrder = (Button) findViewById(R.id.btnSubmitOrder);

        mOrderProductList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = firebaseAuth.getCurrentUser();

        if(mFirebaseUser.getEmail() == null) {

            final String userName = Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName();

            setUserTable(userName);

        }else{

            setUserTable(mFirebaseUser.getEmail().split("@")[0]);
        }
    }

    private BigDecimal calculateTotalPrice(List<OrderProduct> mOrderProductList){

        BigDecimal totalPrice = BigDecimal.ZERO;

        for(OrderProduct product : mOrderProductList){
            BigDecimal itemCost ;

            itemCost = BigDecimal.valueOf(Double.parseDouble(product.getPrice())).multiply(new BigDecimal(product.getQuantity()));
            totalPrice = totalPrice.add(itemCost);
        }
        return totalPrice;
    }
}
