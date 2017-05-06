package com.example.cucutaae.mobileordering10.aboutus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView ivPlaceLogo;
    private TextView txtplaceDescription;
    private TextView txtTimeOpen;
    private TextView txtPlacePhoneNumber;
    private TextView txtRecomandation;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FB_DATABASE_ABOUT_US_PATH);

        initComponents();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    PlaceInfo placeInfo = snapshot.getValue(PlaceInfo.class);

                    if(placeInfo != null) {

                        Picasso.with(getBaseContext()).load(placeInfo.getImageUri()).into(ivPlaceLogo);

                        txtplaceDescription.setText(placeInfo.getPlaceDescription());
                        txtPlacePhoneNumber.setText(placeInfo.getPhoneNumber());
                        txtTimeOpen.setText(placeInfo.getOpenTime());
                        txtRecomandation.setText(placeInfo.getRecomandation());
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
        ivPlaceLogo = (ImageView) findViewById(R.id.ivPlaceLogo);
        txtTimeOpen = (TextView) findViewById(R.id.txtTimeOpen);
        txtplaceDescription = (TextView) findViewById(R.id.txtplaceDescription);
        txtPlacePhoneNumber = (TextView) findViewById(R.id.txtPlacePhoneNumber);
        txtRecomandation = (TextView)findViewById(R.id.txtRecomandation);
    }
}
