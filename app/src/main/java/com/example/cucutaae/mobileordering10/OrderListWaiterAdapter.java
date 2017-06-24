package com.example.cucutaae.mobileordering10;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cucut on 6/18/2017.
 */

public class OrderListWaiterAdapter extends BaseAdapter {

    private FirebaseAuth firebaseAuth;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference refOrders = database.getReference("Orders");

    private Context mContext;
    private List<OrderProduct> mOrderProductList;

    public OrderListWaiterAdapter(Context mContext, List<OrderProduct> mOrderProductList) {
        this.mContext = mContext;
        this.mOrderProductList = mOrderProductList;
    }

    @Override
    public int getCount() {
        return mOrderProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.product_order_forwaiter_item, null);

        TextView tvOrderProductQuantity = (TextView) view.findViewById(R.id.tvOrderProductQuantity);
        TextView tvOrderProductName = (TextView) view.findViewById(R.id.tvOrderProductName);
        TextView tvOrderProductPrice = (TextView) view.findViewById(R.id.tvOrderProductPrice);

        ImageButton ibTakeAnEyeProduct = (ImageButton)  view.findViewById(R.id.ibTakeAnEyeProduct);

        tvOrderProductQuantity.setText(String.valueOf(mOrderProductList.get(position).getQuantity()));
        tvOrderProductName.setText(mOrderProductList.get(position).getProductName());
        tvOrderProductPrice.setText(String.valueOf(mOrderProductList.get(position).getPrice() + "$"));

        ibTakeAnEyeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Product added by: " +
                        mOrderProductList.get(position).getUser(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
