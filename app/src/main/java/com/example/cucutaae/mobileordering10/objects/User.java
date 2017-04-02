package com.example.cucutaae.mobileordering10.objects;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cucut on 4/2/2017.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String profilePictureURI;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String username, String email, String profilePictureURI) {

        this.username = username;
        this.email = email;
        this.profilePictureURI = profilePictureURI;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureURI() {
        return profilePictureURI;
    }

    public void setProfilePictureURI(String profilePictureURI) {
        this.profilePictureURI = profilePictureURI;
    }
}
