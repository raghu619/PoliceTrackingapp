package com.example.android.policetrackingapp;

/**
 * Created by raghvendra on 11/3/18.
 */

public class Police_data {

    String muid;
    String memail;
    String mname;
    String  mphone_no;
    String  maddress;

    Police_data(){

    }

    Police_data(String uid,String email,String name,String phone_no,String address){

        this.muid=uid;
        this.memail=email;
        this.mname=name;

        this.mphone_no=phone_no;
        this.maddress=address;

    }


    public String getMuid(){

        return muid;
    }

    public String getMemail(){

        return memail;

    }

    public String getMname(){

        return mname;
    }
    public String getMphone_no(){

        return mphone_no;
    }

    public String getMaddress(){

        return  maddress;
    }

    public void setMuid(String uid){

        this.muid=uid;
    }

    public void setMemail(String email){

        this.memail=email;
    }

    public void setMname(String name){

        this.mname=name;
    }

    public void setMphone_no(String phone_no){

        this.mphone_no=phone_no;
    }

    public void setMaddress(String address){

        this.maddress=address;
    }


}
