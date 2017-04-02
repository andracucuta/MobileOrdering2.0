package com.example.cucutaae.mobileordering10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.cucutaae.mobileordering10.adapter.UserAdapter;
import com.example.cucutaae.mobileordering10.objects.User;
import com.example.cucutaae.mobileordering10.signin.SignInClientActivity;
import com.example.cucutaae.mobileordering10.signin.SignInWaiterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import adapter.ImageAdapter;
import service.FirebaseStorageService;

public class MainClientActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private ViewFlipper vfPicutres;

    private ImageView ivImg1;
    private ImageView ivImg2;
    private ImageView ivImg3;

    private GridView gridview;

    private TextView textViewUserEmail;
    private ImageButton buttonLogout;

    private ImageView ivUserPicture;

    private FirebaseUser mFirebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,SignInClientActivity.class));
        }

        initFields();

        setImageOnImageView(ivImg1,ivImg2,ivImg3);

        vfPicutres.setOnClickListener(this);

        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                switch (position){
                    case 0:
                        Intent intent = new Intent(MainClientActivity.this, AddProductActivity.class);
                        MainClientActivity.this.startActivity(intent);
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

        if("firebaseLogin".equalsIgnoreCase(type_of_login)){

            textViewUserEmail.setText(" " + mFirebaseUser.getEmail().split("@")[0]);

        }else{

            UserAdapter userAdapter = new UserAdapter();

            User userLogined = userAdapter.userList(mFirebaseUser.getUid());

            String userName = getIntent().getStringExtra("SESSION_USER");

            textViewUserEmail.setText(" " + userName);
           /* ivUserPicture.setImageURI(uri);*/

        }
        buttonLogout = (ImageButton)findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(this);
    }

    public void initFields(){

        ivUserPicture = (ImageView)findViewById(R.id.ivUserPicture);

        vfPicutres = (ViewFlipper)findViewById(R.id.vfPicutres);

        ivImg1 = (ImageView)findViewById(R.id.ivImg1);
        ivImg2 = (ImageView)findViewById(R.id.ivImg2);
        ivImg3 = (ImageView)findViewById(R.id.ivImg3);

        gridview = (GridView) findViewById(R.id.gvMenuBtn);
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
            startActivity(new Intent(this, SignInClientActivity.class));
        }
    }
}
