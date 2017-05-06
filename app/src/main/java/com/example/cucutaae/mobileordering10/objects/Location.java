package com.example.cucutaae.mobileordering10.objects;

/**
 * Created by cucut on 5/5/2017.
 */

public class Location {

    private String latitude;
    private String longitude;
    private String placeName;

    public Location() {
    }

    public Location(String latitude, String longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", placeName='" + placeName + '\'' +
                '}';
    }
}
