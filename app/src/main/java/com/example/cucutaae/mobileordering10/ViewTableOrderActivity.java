package com.example.cucutaae.mobileordering10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cucutaae.mobileordering10.menu.AddProductActivity;
import com.example.cucutaae.mobileordering10.order.Order;
import com.example.cucutaae.mobileordering10.order.OrderProduct;
import com.example.cucutaae.mobileordering10.signin.SignInClientActivity;
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

public class ViewTableOrderActivity extends AppCompatActivity {

    private ListView lvProductList;
    private TextView tvOrderNoOrder;
    private TextView tvOrderTableName;
    private TextView tvOrderTotalAll;
    private Button tvRegisterPayment;

    private OrderListWaiterAdapter adapter;

    private List<OrderProduct> mOrderProductListNew = new ArrayList<>();
    private List<OrderProduct> mOrderProductList = new ArrayList<>();

    private BigDecimal totalBill = BigDecimal.ZERO;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refOrders = database.getReference("Orders");

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_table_order);

        tableName =  getIntent().getStringExtra("TABLE");

        init();

        populateProductListOrdered(tableName);

        tvRegisterPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOrder(tableName);
            }
        });

    }

    private void init() {
        lvProductList = (ListView) findViewById(R.id.lvProductList);
        tvOrderNoOrder = (TextView) findViewById(R.id.tvOrderNoOrder);
        tvOrderTableName = (TextView) findViewById(R.id.tvOrderTableName);
        tvOrderTableName.setText(tableName);
        tvOrderTotalAll = (TextView) findViewById(R.id.tvOrderTotalAll);
        tvRegisterPayment = (Button) findViewById(R.id.tvRegisterPayment);

        mOrderProductList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = firebaseAuth.getCurrentUser();

    }

    private void closeOrder(final String tableName) {

        refOrders.orderByChild("confirmed").equalTo("Yes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Query query = refOrders.orderByChild("table").equalTo(tableName);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            snapshot.getRef().child("state").setValue(3);
                            snapshot.getRef().child("confirmed").setValue("closed");
                            //snapshot.getRef().child("paymentType").setValue("");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



               Toast.makeText(getBaseContext(),"Order closed", Toast.LENGTH_SHORT).show();

                getBaseContext().startActivity(new Intent(ViewTableOrderActivity.this, MainWaiterActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateProductListOrdered(final String tableName) {

        refOrders.orderByChild("confirmed").equalTo("Yes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                if (dataSnapshot.getValue() != null) {

                    mOrderProductList = new ArrayList<OrderProduct>();
                    // mOrderProductListNew = new ArrayList<>();

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

                    totalBill = calculateTotalPrice(mOrderProductListNew);
                    totalBill = totalBill.setScale(2, RoundingMode.CEILING);
                    tvOrderTotalAll.setText(String.valueOf(totalBill) + "$");

                    tvOrderNoOrder.setVisibility(View.GONE);
                    lvProductList.setVisibility(View.VISIBLE);
                    tvRegisterPayment.setVisibility(View.VISIBLE);

                    adapter = new OrderListWaiterAdapter(getApplicationContext(), mOrderProductListNew);

                    lvProductList.setAdapter(adapter);

                } else {
                    lvProductList.setVisibility(View.INVISIBLE);
                    tvRegisterPayment.setVisibility(View.INVISIBLE);
                }

                if(totalBill.compareTo(BigDecimal.ZERO) == 0){
                    lvProductList.setVisibility(View.INVISIBLE);
                    tvRegisterPayment.setVisibility(View.INVISIBLE);
                    tvOrderNoOrder.setVisibility(View.VISIBLE);
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

    private BigDecimal calculateTotalPrice(List<OrderProduct> mTableList){

        BigDecimal totalPrice = BigDecimal.ZERO;

        for(OrderProduct product : mTableList){
            BigDecimal itemCost ;

            itemCost = BigDecimal.valueOf(Double.parseDouble(product.getPrice())).multiply(new BigDecimal(product.getQuantity()));
            totalPrice = totalPrice.add(itemCost);
        }
        return totalPrice;
    }
}
