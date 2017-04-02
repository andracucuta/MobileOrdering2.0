package com.example.cucutaae.mobileordering10.adapter;

import android.util.Log;

import com.example.cucutaae.mobileordering10.objects.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by cucut on 4/2/2017.
 */

public class UserAdapter {

    private DatabaseReference mDatabase;

    private DatabaseReference userCloudEndPoint;

    private static final String TAG = "UserAdapter";

    public void writeNewUser(String userId, String name, String email, String profilePictureURI) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        User user = new User(name, email, profilePictureURI);

        mDatabase.child("Users").child(userId).setValue(user);
    }

/*    public User readUser(String userId) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        User user = new User();

        user.setUsername();
        *//*name, email, profilePictureURI*//*
        return user;
    }*/

    public User userList(String uid) {
      /*  mDatabase = FirebaseDatabase.getInstance().getReference();*/

        final User[] user = {new User()};

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user[0] = dataSnapshot.getValue(User.class);

                Log.d(TAG, "User name: " + user[0].getUsername() + ", email " + user[0].getEmail());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return user[0];
    }
}
