package com.example.cucutaae.mobileordering10.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.MainClientActivity;
import com.example.cucutaae.mobileordering10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpClientActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bRegister;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvSignIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_client);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

//        if(firebaseAuth.getCurrentUser()!=null){
//            //profile activity here
//            finish();
//            startActivity(new Intent(getApplicationContext(),MainClientActivity.class));
//        }

        progressDialog = new ProgressDialog(this);

        bRegister = (Button)findViewById(R.id.buttonRegister);

        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);

        tvSignIn = (TextView) findViewById(R.id.textViewSignIn);

        bRegister.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view == bRegister){
            registerUser();
        }

        if(view == tvSignIn){
            //will open login activity here
            startActivity(new Intent(this,MainClientActivity.class));
        }
    }

    private void registerUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Please enter an email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"Please enter an password", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        //if validation are ok
        // we will show a progress bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            //user is successfuly register and logged in
                            //will start the next activity here
                            Toast.makeText(SignUpClientActivity.this,"Registered  Successfuly",Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(new Intent(getApplicationContext(),MainClientActivity.class));
                        }
                        else{
                            Toast.makeText(SignUpClientActivity.this,"Could not register. :(  Please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
