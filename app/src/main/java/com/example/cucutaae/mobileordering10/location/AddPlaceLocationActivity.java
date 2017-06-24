package com.example.cucutaae.mobileordering10.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPlaceLocationActivity extends AppCompatActivity {

    private EditText etLongitude;
    private EditText etLatitude;
    private EditText etPlaceName;
    private Button btnSubmit;

    private DatabaseReference mDatabaseRef;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refLocation = database.getReference("Location");

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_location);

        init();

        refLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Location locationF = snapshot.getValue(Location.class);

                    if(locationF != null) {

                            etLongitude.setText(locationF.getLongitude());
                            etLatitude.setText(locationF.getLatitude());
                            etPlaceName.setText(locationF.getPlaceName());

                            btnSubmit.setText("Update Location");
                    }

                    Log.v("LOCATION_V", snapshot.getKey() + " " + locationF.toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LOCATION_V", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!areFieldsEmpty()) {
                    location = new Location(etLongitude.getText().toString(),
                            etLatitude.getText().toString(), etPlaceName.getText().toString());

                    mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                    mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild("Location")) {

                                mDatabaseRef.child("Location").setValue(location);

                                Toast.makeText(getBaseContext(), "Location for " + location.getPlaceName()
                                        + " was updated!", Toast.LENGTH_LONG).show();
                            } else {
                                String locationId = mDatabaseRef.push().getKey();

                                mDatabaseRef.child("Location").child(locationId).setValue(location);

                                Toast.makeText(getBaseContext(), "Location for " + location.getPlaceName()
                                        + " added successfuly!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(getBaseContext(), "One of the fields is empty. Pleas fill it in.", Toast.LENGTH_LONG).show();
                }

            }

        });
    }

    private void init() {
        etLongitude = (EditText)findViewById(R.id.etLongitude);
        etLatitude = (EditText) findViewById(R.id.etLatitude);
        etPlaceName = (EditText) findViewById(R.id.etPlaceName);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    private boolean areFieldsEmpty(){

        if(etLongitude.getText().toString().equals("")
                || etLatitude.getText().toString().equals("")
                || etPlaceName.getText().toString().equals("")){
            return true;
        }

        return false;
    }
}
