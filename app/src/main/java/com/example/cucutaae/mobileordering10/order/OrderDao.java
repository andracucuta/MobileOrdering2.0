package com.example.cucutaae.mobileordering10.order;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by cucut on 5/1/2017.
 */

public class OrderDao {

    private DatabaseReference mDatabaseRef;

    public OrderDao() {}

    public void registerTable(final String userName,  Date date, final String table) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateReserveTable = fmt.format(date);

        final Order orderFinal = new Order(userName, 0, dateReserveTable, table, "No", "none");

        String orderId = mDatabaseRef.push().getKey();
        mDatabaseRef.child("Orders").child(orderId).setValue(orderFinal);
    }
}
