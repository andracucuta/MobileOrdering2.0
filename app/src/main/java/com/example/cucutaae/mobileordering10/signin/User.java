package com.example.cucutaae.mobileordering10.signin;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by cucut on 4/2/2017.
 */

@IgnoreExtraProperties
public class User implements Serializable{

    public String userId;
    public String user;
    public String email;
    public String profilePictureURI;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String user, String email, String profilePictureURI) {

        this.user = user;
        this.email = email;
        this.profilePictureURI = profilePictureURI;
    }

    public User(String userId, String user, String email, String profilePictureURI) {
        this.userId = userId;
        this.user = user;
        this.email = email;
        this.profilePictureURI = profilePictureURI;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", user='" + user + '\'' +
                ", email='" + email + '\'' +
                ", profilePictureURI='" + profilePictureURI + '\'' +
                '}';
    }
}
