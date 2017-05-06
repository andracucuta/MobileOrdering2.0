package com.example.cucutaae.mobileordering10.dao;

import com.example.cucutaae.mobileordering10.objects.MenuProduct;
import com.example.cucutaae.mobileordering10.objects.Order;
import com.example.cucutaae.mobileordering10.objects.OrderProduct;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by cucut on 5/1/2017.
 */

public class OrderDao {

    private DatabaseReference mDatabaseRef;

    public OrderDao() {
    }

    public void writeOrder(String userId, String userName,  Date date, String table, List<MenuProduct> productList) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateReserveTable = fmt.format(date);

        Order order = new Order(userName, 0 , dateReserveTable, table, productList);

        String uploadId = mDatabaseRef.push().getKey();

        mDatabaseRef.child("Order").child(uploadId).setValue(order);
    }

    public void updateOrder(String userId, OrderProduct orderProduct) {

/*        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        String uploadId = mDatabaseRef.push().getKey();

        mDatabaseRef.child("Order").push().setValue("OrderList").;

        mDatabaseRef.child("Order").child(userId).push().setValue("OrderList").setValue(orderProduct);*/
    }
}
