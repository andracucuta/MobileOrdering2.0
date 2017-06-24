package com.example.cucutaae.mobileordering10.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cucut on 5/4/2017.
 */

public class OrderListAdapter extends BaseAdapter {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference refOrders = database.getReference("Orders");

    private Context mContext;
    private List<OrderProduct> mOrderProductList;

    public OrderListAdapter(Context mContext, List<OrderProduct> mOrderProductList) {
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
        View view = View.inflate(mContext, R.layout.product_order_item, null);

        TextView tvOrderProductQuantity = (TextView) view.findViewById(R.id.tvOrderProductQuantity);
        TextView tvOrderProductName = (TextView) view.findViewById(R.id.tvOrderProductName);
        TextView tvOrderProductPrice = (TextView) view.findViewById(R.id.tvOrderProductPrice);

        ImageButton ibRemoveProduct = (ImageButton) view.findViewById(R.id.ibRemoveProduct);
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

        ibRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String productName = mOrderProductList.get(position).getProductName();
                String orderKey  = mOrderProductList.get(position).getKey();
                String productKey = mOrderProductList.get(position).getOrderKey();

                //Orders -KmwyLdPgOBZfv5Fk7Ia OrderProducts -KmwyYVxHSgOhFlhjcZI
                final DatabaseReference myRef = database.getReference("Orders/"+orderKey);
                myRef.child("OrderProducts").child(productKey).setValue(null);


                mOrderProductList.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
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
