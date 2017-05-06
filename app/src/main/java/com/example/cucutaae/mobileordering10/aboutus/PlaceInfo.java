package com.example.cucutaae.mobileordering10.aboutus;

/**
 * Created by cucut on 5/6/2017.
 */

public class PlaceInfo {

    private String placeName;
    private String placeDescription;
    private String phoneNumber;
    private String wifiName;
    private String wifiPassword;
    private String imageUri;
    private String openTime;
    private String recomandation;

    public PlaceInfo(){

    }

    public PlaceInfo(String placeName, String placeDescription,String phoneNumber,
                     String wifiName, String wifiPassword, String imageUri,String openTime,String recomandation) {
        this.placeName = placeName;
        this.placeDescription = placeDescription;
        this.phoneNumber = phoneNumber;
        this.wifiName = wifiName;
        this.wifiPassword = wifiPassword;
        this.imageUri = imageUri;
        this.openTime = openTime;
        this.recomandation = recomandation;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getRecomandation() {
        return recomandation;
    }

    public void setRecomandation(String recomandation) {
        this.recomandation = recomandation;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "placeName='" + placeName + '\'' +
                ", placeDescription='" + placeDescription + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", wifiName='" + wifiName + '\'' +
                ", wifiPassword='" + wifiPassword + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", openTime='" + openTime + '\'' +
                ", recomandation='" + recomandation + '\'' +
                '}';
    }
}
