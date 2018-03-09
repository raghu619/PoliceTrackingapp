package com.example.android.policetrackingapp;

/**
 * Created by raghvendra on 14/1/18.
 */

public  class Current_Location {
    private  String mail_id;
    private  String longitude;
    private  String latitude;
    private  String maddress;
    private  String mtime;



    public Current_Location(){

    }


    public Current_Location(String longitude,String latitude)
    {
        this.longitude=longitude;
        this.latitude=latitude;

    }
    public Current_Location(String longitude,String latitude,String mail_id,String address)
    {
        this.longitude=longitude;
        this.latitude=latitude;
        this.mail_id=mail_id;
        this.maddress=address;
    }
    public Current_Location(String longitude,String latitude,String mail_id,String address,String time)
    {
        this.longitude=longitude;
        this.latitude=latitude;
        this.mail_id=mail_id;
        this.maddress=address;
        this.mtime=time;
    }

    public  String getLongitude(){

        return longitude;

    }

    public String getMaddress(){


        return maddress;
    }


    public  String getMtime(){


        return mtime;
    }

    public  String getMail_id(){

        return mail_id;

    }


    public String getLatitude(){

        return latitude;
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

    public  void  setMtime(String time){

        this.mtime=time;

    }

    public  void setMaddress(String address){

        this.maddress=address;

    }


}
