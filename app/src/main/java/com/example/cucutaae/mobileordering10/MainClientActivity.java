package com.example.cucutaae.mobileordering10;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.example.cucutaae.mobileordering10.aboutus.AboutUsActivity;
import com.example.cucutaae.mobileordering10.location.LocationActivity;
import com.example.cucutaae.mobileordering10.menu.CategoryListClientActivity;
import com.example.cucutaae.mobileordering10.order.OrderActivity;
import com.example.cucutaae.mobileordering10.signin.SignInClientActivity;
import com.example.cucutaae.mobileordering10.tableReservation.TableReservationActivity;
import com.example.cucutaae.mobileordering10.wifi.WifiActivity;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.example.cucutaae.mobileordering10.menu.ImageAdapter;
import service.FirebaseStorageService;

public class MainClientActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewFlipper vfPicutres;
    private ImageView ivUserPicture;
    private ImageView ivImg1;
    private ImageView ivImg2;
    private ImageView ivImg3;
    private ImageView ivImg4;
    private GridView gridview;
    private TextView textViewUserEmail;
    private ImageView buttonLogout;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        Firebase.setAndroidContext(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user== null) {
            finish();
            startActivity(new Intent(this, SignInClientActivity.class));
        } else {

            initFields();

            setImageOnImageView(ivImg1, ivImg2, ivImg3, ivImg4);

            vfPicutres.setOnClickListener(this);

            gridview.setAdapter(new ImageAdapter(this));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    switch (position) {
                        case 0:
                            Intent intentMenu = new Intent(MainClientActivity.this, CategoryListClientActivity.class);
                            intentMenu.putExtra("USER_TYPE", "client");
                            MainClientActivity.this.startActivity(intentMenu);
                            break;
                        case 1:
                            Intent intentOrder = new Intent(MainClientActivity.this, OrderActivity.class);
                            MainClientActivity.this.startActivity(intentOrder);
                            break;
                        case 2:
                            Intent intentReserveTable = new Intent(MainClientActivity.this, TableReservationActivity.class);
                            MainClientActivity.this.startActivity(intentReserveTable);
                            break;
                        case 3:
                            String number = "0754899871";
                            Intent intentCallUs = new Intent(Intent.ACTION_DIAL);
                            intentCallUs.setData(Uri.parse("tel:" + number));
                            if (ActivityCompat.checkSelfPermission(MainClientActivity.this,
                                    android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                MainClientActivity.this.startActivity(intentCallUs);
                                break;
                            }
                            break;

                        case 4:
                            Intent intentLocation = new Intent(MainClientActivity.this, LocationActivity.class);
                            MainClientActivity.this.startActivity(intentLocation);
                            break;
                        case 5:
                            Intent intentRequestPaymentActivity = new Intent(MainClientActivity.this, RequestPaymentActivity.class);
                            MainClientActivity.this.startActivity(intentRequestPaymentActivity);
                            break;
                        case 6:
                            Intent intentWifi = new Intent(MainClientActivity.this, WifiActivity.class);
                            MainClientActivity.this.startActivity(intentWifi);
                            break;
                        case 7:
                            Intent intentAboutUs = new Intent(MainClientActivity.this, AboutUsActivity.class);
                            MainClientActivity.this.startActivity(intentAboutUs);
                            break;
                        default:
                            Toast.makeText(MainClientActivity.this, "" + position,
                                    Toast.LENGTH_SHORT).show();
                    }
                }
            });

            String type_of_login = getIntent().getStringExtra("TYPE_OF_LOGIN");

            textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

            mFirebaseUser = firebaseAuth.getCurrentUser();

            if ("firebaseLogin".equalsIgnoreCase(type_of_login)) {

                textViewUserEmail.setText(" " + mFirebaseUser.getEmail().split("@")[0]);

            } else {

                textViewUserEmail.setText(" " + mFirebaseUser.getDisplayName());

                Picasso.with(getBaseContext()).load(mFirebaseUser.getPhotoUrl()).into(ivUserPicture);

            }
            buttonLogout = (ImageView) findViewById(R.id.buttonLogout);
            buttonLogout.setOnClickListener(this);
        }
    }

    public void initFields(){

        ivUserPicture = (ImageView)findViewById(R.id.ivUserPicture);

        vfPicutres = (ViewFlipper)findViewById(R.id.vfPicutres);

        vfPicutres.startFlipping();
        vfPicutres.setFlipInterval(3000);

        ivImg1 = (ImageView)findViewById(R.id.ivImg1);
        ivImg2 = (ImageView)findViewById(R.id.ivImg2);
        ivImg3 = (ImageView)findViewById(R.id.ivImg3);
        ivImg4 = (ImageView)findViewById(R.id.ivImg4);

        gridview = (GridView) findViewById(R.id.gvMenuBtn);
    }

    public void setImageOnImageView(ImageView... ivList){
        int i = 0;

        for(ImageView iv : ivList){

            FirebaseStorageService.getImageFromFirebaseStorage(iv,
                    "gs://mobileorderingpj.appspot.com/FirstClientPagePictures", "ivImg"+ ++i + ".png");
        }
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            //firebaseAuth.signOut();

            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();

            finish();

            Intent intentSignOut = new Intent(MainClientActivity.this, SignInClientActivity.class);
            intentSignOut.putExtra("USER_SIGN_OUT", "signOutUser");
            MainClientActivity.this.startActivity(intentSignOut);
        }
    }
}
