package com.example.cucutaae.mobileordering10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.cucutaae.mobileordering10.signin.SignInWaiterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import service.FirebaseStorageService;

public class MainClientActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ViewFlipper vfPicutres;

    private ImageView ivImg1;
    private ImageView ivImg2;
    private ImageView ivImg3;

    private TextView textViewUserEmail;
    private Button buttonLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,SignInWaiterActivity.class));
        }

        initFields();

        setImageOnImageView(ivImg1,ivImg2,ivImg3);

        vfPicutres.setOnClickListener(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView)findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome " + user.getEmail()+"!");
        buttonLogout = (Button)findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
    }

    public void initFields(){

        vfPicutres = (ViewFlipper)findViewById(R.id.vfPicutres);

        ivImg1 = (ImageView)findViewById(R.id.ivImg1);
        ivImg2 = (ImageView)findViewById(R.id.ivImg2);
        ivImg3 = (ImageView)findViewById(R.id.ivImg3);
    }

    public void setImageOnImageView(ImageView... ivList){
        int i = 0;

        for(ImageView iv : ivList){

            FirebaseStorageService.getImageFromFirebaseStorage(iv, "gs://mobileorderingpj.appspot.com/FirstClientPagePictures", "ivImg"+ ++i + ".png");
        }
    }

    @Override
    public void onClick(View view){
        if(view == vfPicutres){
            vfPicutres.startFlipping();
            vfPicutres.setFlipInterval(3000);
        }
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,SignInWaiterActivity.class));
        }
    }
}
