package com.example.cucutaae.mobileordering10.menu;


/**
 * Created by cucut on 4/30/2017.
 */

public class Review {

    private String userName;
    private String postDate;
    private String review;
    private Float score;

    public Review() {
    }

    public Review(String userName, String postDate, String review, Float score) {
        this.userName = userName;
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


    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
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
                ", postDate=" + postDate +
                ", review='" + review + '\'' +
                ", score=" + score +
                '}';
    }
}
