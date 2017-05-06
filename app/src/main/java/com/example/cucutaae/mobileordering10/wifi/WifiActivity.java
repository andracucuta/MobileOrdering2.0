package com.example.cucutaae.mobileordering10.wifi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.aboutus.PlaceInfo;
import com.example.cucutaae.mobileordering10.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class WifiActivity extends AppCompatActivity {

    private TextView txtWifiName;
    private TextView txtWifiPasswork;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FB_DATABASE_ABOUT_US_PATH);

        initComponents();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    PlaceInfo placeInfo = snapshot.getValue(PlaceInfo.class);

                    if(placeInfo != null) {

                        txtWifiName.setText(placeInfo.getWifiName());
                        txtWifiPasswork.setText(placeInfo.getWifiPassword());

                    }

                    Log.v("PLACE_INFO_V", snapshot.getKey() + " " + placeInfo.toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("PLACE_INFO_V", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
    private void initComponents(){

        txtWifiName = (TextView) findViewById(R.id.txtWifiName);
        txtWifiPasswork = (TextView) findViewById(R.id.txtWifiPasswork);
    }
}
