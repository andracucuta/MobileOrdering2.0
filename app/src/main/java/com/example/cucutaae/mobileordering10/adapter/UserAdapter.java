package com.example.cucutaae.mobileordering10.adapter;

import com.example.cucutaae.mobileordering10.objects.User;
import com.example.cucutaae.mobileordering10.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by cucut on 4/2/2017.
 */

public class UserAdapter {


    private DatabaseReference mDatabaseRef;
    private List<User> userList;

    private static final String TAG = "UserAdapter";


    public void writeNewUser(String userId, String name, String email, String profilePictureURI) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        User user = new User(name, email, profilePictureURI);

        mDatabaseRef.child("Users").child(userId).setValue(user);
    }


    public User userList(String uid) {

        final User userReturned = new User();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FB_DATABASE_USER_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return userReturned;
    }
}
