package com.wormos.intrester;

public class CreatePlaceModel {

    String place,purl;

    public CreatePlaceModel(String place, String purl) {
        this.place = place;
        this.purl = purl;
    }

    public CreatePlaceModel() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
