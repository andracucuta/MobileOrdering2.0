package com.example.cucutaae.mobileordering10.location;

import android.util.Log;
import com.example.cucutaae.mobileordering10.location.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by cucut on 5/5/2017.
 */

public class LocationDao {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference refLocation = database.getReference("Location");

    private Location location;

    public Location getLocation(){

        refLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    location = snapshot.getValue(Location.class);

                    Log.v("LOCATION_V", snapshot.getKey() + " " + location.toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LOCATION", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        return location;
    }
}
