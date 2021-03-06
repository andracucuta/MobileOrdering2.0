package com.example.cucutaae.mobileordering10.dao;

import com.example.cucutaae.mobileordering10.signin.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by cucut on 4/2/2017.
 */

public class UserDao{

    private DatabaseReference mDatabaseRef;
    private List<User> userList;

    private static final String TAG = "UserDao";


    public void writeNewUser(String userId, String name, String email, String profilePictureURI) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        User user = new User(name, email, profilePictureURI);

        mDatabaseRef.child("Users").child(userId).setValue(user);
    }
}
