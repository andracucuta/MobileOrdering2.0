package com.example.cucutaae.mobileordering10.objects;

import java.util.Date;

/**
 * Created by cucut on 4/30/2017.
 */

public class Review {

    private String userName;
    private String profilePictureUrl;
    private Date postDate;
    private String review;
    private Float score;

    public Review(String userName, String profilePictureUrl, Date postDate, String review, Float score) {
        this.userName = userName;
        this.profilePictureUrl = profilePictureUrl;
        this.postDate = postDate;
        this.review = review;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Review{" +
                "userName='" + userName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", postDate=" + postDate +
                ", review='" + review + '\'' +
                ", score=" + score +
                '}';
    }
}
