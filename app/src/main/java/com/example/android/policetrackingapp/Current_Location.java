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
    private  String mphone_no;
    private  String  musername;
    private  String  muid;
    private  String mstatus;



    public Current_Location(){

    }


    public Current_Location(String longitude,String latitude)
    {
        this.longitude=longitude;
        this.latitude=latitude;

    }
    public Current_Location(String longitude,String latitude,String mail_id,String address,String status)
    {
        this.longitude=longitude;
        this.latitude=latitude;
        this.mail_id=mail_id;
        this.maddress=address;
        this.mstatus=status;
    }

    public Current_Location(String longitude,String latitude,String mail_id,String address,String time,String Phone_no,String Username,String uid,String status)
    {
        this.longitude=longitude;
        this.latitude=latitude;
        this.mail_id=mail_id;
        this.maddress=address;
        this.mtime=time;
        this.mphone_no=Phone_no;
        this.musername=Username;
        this.muid=uid;
        this.mstatus=status;
    }

    public  String getLongitude(){

        return longitude;

    }

    public String getMstatus(){

        return mstatus;
    }

    public String getMuid(){

        return muid;
    }

    public String getMaddress(){


        return maddress;
    }
    public String getMusername(){


        return musername;
    }

    public String getMphone_no() {

        return mphone_no;
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

    public void setMuid(String uid){

        this.muid=uid;
    }

    public void setLatitude(String latitude){

        this.latitude=latitude;
    }
    public void setMusername(String Username){

        this.musername=Username;
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

    public void setMphone_no(String Phone_no){

        this.mphone_no=Phone_no;
    }


    public void setMstatus(String status){

        this.mstatus=status;
    }




}
