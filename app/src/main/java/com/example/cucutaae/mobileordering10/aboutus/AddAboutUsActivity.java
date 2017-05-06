package com.example.cucutaae.mobileordering10.aboutus;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddAboutUsActivity extends AppCompatActivity {

    private Button btnUpload;
    private ImageView ivPlaceLogo;
    private EditText txtPlaceName;
    private EditText txtplaceDescription;
    private EditText txtPlacePhoneNumber;
    private EditText txtWifiName;
    private EditText txtWifiPasswork;
    private EditText txtTimeOpen;
    private EditText txtRecomandation;

    private Button btnSave;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_about_us);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FB_DATABASE_ABOUT_US_PATH);

        initComponents();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    PlaceInfo placeInfo = snapshot.getValue(PlaceInfo.class);

                    if(placeInfo != null) {

                        btnUpload.setText("Change Place Logo");

                        Picasso.with(getBaseContext()).load(placeInfo.getImageUri()).into(ivPlaceLogo);

                        txtPlaceName.setText(placeInfo.getPlaceName());
                        txtplaceDescription.setText(placeInfo.getPlaceDescription());
                        txtPlacePhoneNumber.setText(placeInfo.getPhoneNumber());
                        txtWifiName.setText(placeInfo.getWifiName());
                        txtWifiPasswork.setText(placeInfo.getWifiPassword());
                        txtTimeOpen.setText(placeInfo.getOpenTime());
                        txtRecomandation.setText(placeInfo.getRecomandation());

                        btnSave.setText("Update info");
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
        btnUpload = (Button) findViewById(R.id.btnUpload);
        ivPlaceLogo = (ImageView) findViewById(R.id.ivPlaceLogo);
        txtTimeOpen = (EditText) findViewById(R.id.txtTimeOpen);
        txtPlaceName = (EditText) findViewById(R.id.txtPlaceName) ;
        txtplaceDescription = (EditText) findViewById(R.id.txtplaceDescription);
        txtPlacePhoneNumber = (EditText) findViewById(R.id.txtPlacePhoneNumber);
        txtWifiName = (EditText) findViewById(R.id.txtWifiName);
        txtWifiPasswork = (EditText) findViewById(R.id.txtWifiPasswork);
        txtRecomandation = (EditText)findViewById(R.id.txtRecomandation);

        btnSave = (Button) findViewById(R.id.btnSave);
    }

    public void btnBrowse_Click(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), Constants.REQUEST_CODE);
    }

    @SuppressWarnings("VisibleForTests")
    public void btnSave_Click(View v) {
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading logo");
            dialog.show();

            //Get the storage reference
            StorageReference ref = mStorageRef.child(Constants.FB_STORAGE_ABOUT_US_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

            //Add file to reference

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Dimiss dialog when success
                    dialog.dismiss();
                    //Display success toast msg

                    PlaceInfo placeInfo = new PlaceInfo(txtPlaceName.getText().toString(),txtplaceDescription.getText().toString(),
                            txtPlacePhoneNumber.getText().toString(), txtWifiName.getText().toString(),
                            txtWifiPasswork.getText().toString(),taskSnapshot.getDownloadUrl().toString(),
                            txtTimeOpen.getText().toString(),txtRecomandation.getText().toString());

                    if(btnSave.getText().toString().equalsIgnoreCase("Save")) {
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(placeInfo);

                        Toast.makeText(getApplicationContext(), "About Us Information saved", Toast.LENGTH_SHORT).show();

                    }else{
                        mDatabaseRef.child("AboutUs").setValue(placeInfo);

                        Toast.makeText(getApplicationContext(), "About Us Information updated", Toast.LENGTH_SHORT).show();

                    }

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Dimiss dialog when error
                            dialog.dismiss();
                            //Display err toast msg
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //Show upload progress

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                ivPlaceLogo.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
