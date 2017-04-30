package com.example.cucutaae.mobileordering10.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cucutaae.mobileordering10.MainClientActivity;
import com.example.cucutaae.mobileordering10.MainWaiterActivity;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.adapter.UserAdapter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignInClientActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bSignin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvSignup;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "SignInClientActivity";

    private String typeOfLogin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Newer version of Firebase
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        setContentView(R.layout.activity_sign_in_client);

        firebaseAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            //profile activity here
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),MainClientActivity.class));
        }else {

            progressDialog = new ProgressDialog(this);

            etEmail = (EditText) findViewById(R.id.editTextEmail);
            etPassword = (EditText) findViewById(R.id.editTextPassword);

            //TO::DO Delete this at the end
            etEmail.setText("cucuta.andra@yahoo.com");
            etPassword.setText("pispis93");
            //End TO::DO

            bSignin = (Button) findViewById(R.id.buttonSignIn);

            tvSignup = (TextView) findViewById(R.id.textViewSignUp);


            //start facebook

            mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_facebookButton);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);

                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                    // ...
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                    // ...
                }
            });


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());

                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };

            //end facebook

            bSignin.setOnClickListener(this);
            tvSignup.setOnClickListener(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInClientActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{

                            UserAdapter userAdapter = new UserAdapter();

                            String userId = user.getUid();
                            String name = user.getDisplayName() == null ? "" : user.getDisplayName();
                            String email = user.getEmail() == null ? "" : user.getEmail();
                            String profileImage = user.getPhotoUrl().toString() ;

                            userAdapter.writeNewUser(userId, name, email, profileImage);

                            Intent intent = new Intent(getBaseContext(), MainClientActivity.class);

                            typeOfLogin = "facebookLogin";
                            intent.putExtra("TYPE_OF_LOGIN", typeOfLogin);

                            startActivity(intent);

                            /*startActivity(new Intent(getApplicationContext(),MainClientActivity.class));*/
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view){
        if(view == bSignin){
            userLogin();
        }

        if(view == tvSignup){
            finish();
            startActivity(new Intent(this,SignUpClientActivity.class));
        }
    }


    private void userLogin(){

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this,"Please enter an email", Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(password)){

            Toast.makeText(this,"Please enter an password", Toast.LENGTH_SHORT).show();

            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            finish();
                            ///

                            Intent intent = new Intent(getBaseContext(), MainClientActivity.class);

                            typeOfLogin = "firebaseLogin";

                            intent.putExtra("TYPE_OF_LOGIN", typeOfLogin);
                            startActivity(intent);

                            /*startActivity(new Intent(getApplicationContext(),MainClientActivity.class));*/
                        }
                    }
                });
    }
}
