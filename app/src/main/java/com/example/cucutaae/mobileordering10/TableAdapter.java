package com.example.cucutaae.mobileordering10;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cucutaae.mobileordering10.order.Order;
import com.example.cucutaae.mobileordering10.order.OrderActivity;
import com.example.cucutaae.mobileordering10.order.OrderProduct;
import com.google.firebase.auth.FirebaseAuth;
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

import static com.example.cucutaae.mobileordering10.R.id.lvTableList;

/**
 * Created by cucut on 6/18/2017.
 */

public class TableAdapter extends BaseAdapter {

    private FirebaseAuth firebaseAuth;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference refOrders = database.getReference("Orders");

    private List<OrderProduct> mOrderProductList = new ArrayList<>();

    private Context mContext;
    private List<Table> mTableList;

    public TableAdapter(Context mContext, List<Table> mTableList) {
        this.mContext = mContext;
        this.mTableList = mTableList;
    }

    @Override
    public int getCount() {
        return mTableList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = View.inflate(mContext, R.layout.table_item, null);

        TextView btnTableName = (TextView) view.findViewById(R.id.btnTableName);
        ImageButton ibTakeAnEyeProduct = (ImageButton) view.findViewById(R.id.ibTakeAnEyeProduct);
        ImageButton ibPaymentCalled = (ImageButton) view.findViewById(R.id.ibPaymentCalled);

        btnTableName.setText(String.valueOf(mTableList.get(position).getName()));

        verifyIfPaymentRequired(String.valueOf(mTableList.get(position).getName()), ibPaymentCalled);

        ibTakeAnEyeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mContext.startActivity(new Intent(mContext, ViewTableOrderActivity.class));

                Intent intent = new Intent(mContext, ViewTableOrderActivity.class);

                Table item = (Table) mTableList.get(position);

                intent.putExtra("TABLE",item.getName());

                mContext.startActivity(intent);
            }
        });

        return view;
    }

    private void verifyIfPaymentRequired(final String tableName, final ImageButton ibPaymentCalled) {

        refOrders.orderByChild("state").equalTo(0).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Query query = refOrders.orderByChild("table").equalTo(tableName);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            Order order = snapshot.getValue(Order.class);

                            Map<Object, Map<String, Object>> map = (Map<Object, Map<String, Object>>) dataSnapshot.getValue();

                            for (Map.Entry<Object, Map<String, Object>> entry : map.entrySet()) {
                                Log.v("DATA_V_MAP", entry.getKey().toString() + "/" + entry.getValue());

                                String [] values = entry.getValue().toString().replace("{", "").replace("}","").trim().split(",");

                                for(String s : values){
                                    String [] elem = s.trim().split("=");

                                    if(elem[0].equalsIgnoreCase("confirmed")){
                                        if(elem[1].equalsIgnoreCase("Yes")){
                                            if ("cash".equals(order.getPaymentType())) {
                                                ibPaymentCalled.setVisibility(View.VISIBLE);
                                                ibPaymentCalled.setImageResource(R.drawable.money);
                                            } else if ("card".equals(order.getPaymentType())) {
                                                ibPaymentCalled.setVisibility(View.VISIBLE);
                                                ibPaymentCalled.setImageResource(R.drawable.creditcard);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
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
}
