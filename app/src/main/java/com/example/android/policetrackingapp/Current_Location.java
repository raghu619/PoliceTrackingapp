package com.example.android.policetrackingapp;

/**
 * Created by raghvendra on 14/1/18.
 */

public  class Current_Location {
    private  String mail_id;
    private  String longitude;
    private  String latitude;
    private  String photoUrl;


    public Current_Location(){
    }


    public Current_Location(String longitude, String latitude)
    {
        this.longitude=longitude;
        this.latitude=latitude;

    }
    public Current_Location(String longitude, String latitude, String mail_id)
    {
        this.longitude=longitude;
        this.latitude=latitude;
        this.mail_id=mail_id;
    }


    public  String getLongitude(){

        return longitude;

    }


    public  String getMail_id(){

        return mail_id;

    }


    public String getLatitude(){

        return latitude;
    }

    public String getPhotoUrl(){

        return photoUrl;
    }

    public void setLongitude(String longitude){

        this.longitude=longitude;
    }

    public void setLatitude(String latitude){

        this.latitude=latitude;
    }

    public void setMail_id(String mail_id){

        this.mail_id=mail_id;
    }

    public void  setPhotoUrl(String photoUrl){

        this.photoUrl=photoUrl;
    }

}
