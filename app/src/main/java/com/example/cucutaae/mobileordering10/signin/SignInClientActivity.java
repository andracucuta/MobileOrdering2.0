package com.example.cucutaae.mobileordering10.signin;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.dao.UserDao;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Arrays;

public class SignInClientActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bSignin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvSignup;

    private ProgressDialog progressDialog;
    private CallbackManager mCallbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "SignInClientActivity";

    private String typeOfLogin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Newer version of Firebase
        String userType =  getIntent().getStringExtra("USER_SIGN_OUT");

        if(!"signOutUser".equalsIgnoreCase(userType)) {
            if (!FirebaseApp.getApps(this).isEmpty()) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }
        }

        setContentView(R.layout.activity_sign_in_client);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //profile activity here
           // firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), MainClientActivity.class));
        } else {

            progressDialog = new ProgressDialog(this);

            etEmail = (EditText) findViewById(R.id.editTextEmail);
            etPassword = (EditText) findViewById(R.id.editTextPassword);

            //TO::DO Delete this at the end
            etEmail.setText("cucuta.andra@yahoo.com");
            etPassword.setText("pispis93");
            //End TO::DO

            bSignin = (Button) findViewById(R.id.buttonSignIn);

            tvSignup = (TextView) findViewById(R.id.textViewSignUp);


            mCallbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_facebookButton);
            loginButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email", "user_birthday"));
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
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());

                    } else {
                        // User is signed out
                        //firebaseAuth.signOut();
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
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
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInClientActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            UserDao userDao = new UserDao();

                            String userId = user.getUid();
                            String name = user.getDisplayName() == null ? "" : user.getDisplayName();
                            String email = user.getEmail() == null ? "" : user.getEmail();
                            String profileImage = user.getPhotoUrl().toString();

                            userDao.writeNewUser(userId, name, email, profileImage);

                            Intent intent = new Intent(getBaseContext(), MainClientActivity.class);

                            typeOfLogin = "facebookLogin";
                            intent.putExtra("TYPE_OF_LOGIN", typeOfLogin);

                            startActivity(intent);
                        }
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
    public void onClick(View view) {
        if (view == bSignin) {
            userLogin();
        }

        if (view == tvSignup) {
            finish();
            startActivity(new Intent(this, SignUpClientActivity.class));
        }
    }


    private void userLogin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();

            return;
        }

        if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Please enter an password", Toast.LENGTH_SHORT).show();

            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {

                            UserDao userDao = new UserDao();
                            userDao.writeNewUser(firebaseAuth.getCurrentUser().getUid(), firebaseAuth.getCurrentUser().getDisplayName(), firebaseAuth.getCurrentUser().getEmail(), firebaseAuth.getCurrentUser().getProviderId());

                            finish();

                            Intent intent = new Intent(getBaseContext(), MainClientActivity.class);

                            typeOfLogin = "firebaseLogin";

                            intent.putExtra("TYPE_OF_LOGIN", typeOfLogin);
                            startActivity(intent);

                        }
                    }
                });
    }
}
