package com.example.android.policetrackingapp.Map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by raghvendra on 18/3/18.
 */

public class DistanceMap extends AsyncTask<LatLng,Void,String> {
    public String origin_address;
    public   String destination_address;

    private  static URL url;
    Context mContext;
    public  String Time="";
    public  boolean completed=false;

    public interface OnUpdateListener {
        public void onUpdate(String time);
    }

    OnUpdateListener listener;

   public DistanceMap(Context context){
        mContext=context;

    }

    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onPostExecute(String s) {

       if(listener!=null){

           listener.onUpdate(s);
       }

        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(LatLng... latLngs) {
        LatLng origin=latLngs[0];
        LatLng destination =latLngs[1];

        String responseString = "";
        String time=" ";

        url=getRequestUrl(origin,destination);
        try {
            responseString=requestDirection(url);
            time=getingjsonresponse(responseString);
            Time=time;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return time;
    }


    private URL getRequestUrl(LatLng origin,LatLng dest) {
        String mode="driving";
        Geocoder geocoder=new Geocoder(mContext);
        try{
            List<Address> addressList1=geocoder.getFromLocation(origin.latitude,origin.longitude,1);
            //origin_address=addressList1.get(0).getSubLocality()+"|";
            origin_address=addressList1.get(0).getLocality()+"|";


            origin_address+=addressList1.get(0).getCountryName();

        }
        catch (IOException e){
            e.printStackTrace();
        }

        try{
            List<Address>addressList2=geocoder.getFromLocation(dest.latitude,dest.longitude,1);
            //destination_address=addressList2.get(0).getSubLocality()+"|";
            destination_address=addressList2.get(0).getLocality()+"|";

            destination_address+=addressList2.get(0).getCountryName();
        }
        catch (IOException e){
            e.printStackTrace();
        }


        String baseURL="https://maps.googleapis.com/maps/api/distancematrix/json";
        Uri builtUri=Uri.parse(baseURL).buildUpon().appendQueryParameter("origins",origin_address)
                .appendQueryParameter("destinations",destination_address).appendQueryParameter("mode",mode).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


//
    }


    private String requestDirection(URL reqUrl) throws IOException {

        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection=(HttpURLConnection)reqUrl.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;


    }


public String getTime(){

       return Time;
}






    public  String getingjsonresponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String time="";
        JSONArray rows=jsonObject.getJSONArray("rows");
        JSONObject jsteps=null;

        JSONArray jLegs = ((JSONObject) rows.getJSONObject(0)).getJSONArray("elements");


        jsteps = ((JSONObject) jLegs.getJSONObject(0)).getJSONObject("duration");




        time=jsteps.getString("text");

        return  time;

    }





}
