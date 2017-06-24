package com.example.cucutaae.mobileordering10.signin;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cucutaae.mobileordering10.R;

public class ChooseProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivWaiter;
    private ImageView ivServiceBell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();

        if (config.smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_choose_profile_tablet);
        }
        else
        {
            setContentView(R.layout.activity_choose_profile_phone);
        }

        ivWaiter = (ImageView) findViewById(R.id.ivWaiter);
        ivServiceBell = (ImageView) findViewById(R.id.ivServiceBell);

        ivWaiter.setOnClickListener(this);
        ivServiceBell.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){

        switch(view.getId()){
            case R.id.ivWaiter:
                /*Toast.makeText(ChooseProfileActivity.this,
                        "Not yet implemented!", Toast.LENGTH_SHORT).show();*/
                startActivity(new Intent(this,SignInWaiterActivity.class));
                break;
            case R.id.ivServiceBell:
                startActivity(new Intent(this,SignInClientActivity.class));
                break;
        }
    }
}
