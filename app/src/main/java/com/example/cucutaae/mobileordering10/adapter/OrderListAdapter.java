package com.example.cucutaae.mobileordering10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.objects.OrderProduct;
import java.util.List;

/**
 * Created by cucut on 5/4/2017.
 */

public class OrderListAdapter extends BaseAdapter {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.product_order_item, null);

        TextView tvOrderProductQuantity = (TextView) view.findViewById(R.id.tvOrderProductQuantity);
        TextView tvOrderProductName = (TextView) view.findViewById(R.id.tvOrderProductName);
        ImageButton ibRemoveProduct = (ImageButton) view.findViewById(R.id.ibRemoveProduct);

        tvOrderProductQuantity.setText(mOrderProductList.get(position).getQuantity());
        tvOrderProductName.setText(mOrderProductList.get(position).getProductName());

        return view;
    }


}
