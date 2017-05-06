package com.example.cucutaae.mobileordering10.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.adapter.OrderListAdapter;
import com.example.cucutaae.mobileordering10.objects.OrderProduct;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private ListView lvProductList;
    private TextView tvOrderTotal;
    private Button btnSubmitOrder;

    private OrderListAdapter adapter;
    private List<OrderProduct> mOrderProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        init();

        OrderProduct prod1 = new OrderProduct(1,"Product1");
        OrderProduct prod2 = new OrderProduct(2,"Product2");
        OrderProduct prod3 = new OrderProduct(3,"Product3");
        OrderProduct prod4 = new OrderProduct(4,"Product4");

        mOrderProductList.add(prod1);
        mOrderProductList.add(prod2);
        mOrderProductList.add(prod3);
        mOrderProductList.add(prod4);

        adapter = new OrderListAdapter(getApplicationContext(), mOrderProductList);

        lvProductList.setAdapter(adapter);
        lvProductList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void init() {
        lvProductList = (ListView) findViewById(R.id.lvProductList);
        tvOrderTotal = (TextView) findViewById(R.id.tvOrderTotal);
        btnSubmitOrder = (Button) findViewById(R.id.btnSubmitOrder);

        mOrderProductList = new ArrayList<>();
    }
}
