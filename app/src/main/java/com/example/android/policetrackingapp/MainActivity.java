package com.example.android.policetrackingapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.policetrackingapp.Map.MapActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener
       {

           public static final String ANONYMOUS="anonymous";
           public static final int RC_SIGN_IN = 1;

    private static final String TAG = "ViewDatabase";
    private  static  LatLng current;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private static List<Current_Location>  Current_long;
    private DatabaseReference myRef;
    private  String UserId;
    private ListView mListView;
    private static ArrayList<String> arraystore;
    private FirebaseAuth mFirebseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    int count=0;
    static  double  mlatitude;
    static double mlongitude;
    private static final int LOCATION_REQUEST = 500;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGoogleApiClient =new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //LayoutInflater inflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view=inflater.inflate(R.layout.content_main,null);
        mListView =findViewById(R.id.listview);
        mFirebseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("DATA");
        arraystore=new ArrayList<>();
        List<Current_Location>  Current_long=new ArrayList<>();

        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){

                    UserId =user.getEmail();
                    Toast.makeText(MainActivity.this,"You're now signed in. Welcome to My Safety app .",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).build(),
                            RC_SIGN_IN);


                }
            }
        };









       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               showData(dataSnapshot);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });







    }

           @Override
           public void onBackPressed() {
               DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
               if (drawer.isDrawerOpen(GravityCompat.START)) {
                   drawer.closeDrawer(GravityCompat.START);
               } else {
                   super.onBackPressed();
               }
           }

           @Override
           protected void onActivityResult(int requestCode, int resultCode, Intent data) {
               super.onActivityResult(requestCode, resultCode, data);
               if(requestCode==RC_SIGN_IN){

                   if (resultCode == RESULT_OK) {
                       // Sign-in succeeded, set up the UI
                       Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();

                   } else if (resultCode == RESULT_CANCELED) {
                       // Sign in was canceled by the user, finish the activity
                       Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                       finish();
                   }
               }

           }

           @Override
           protected void onResume() {
               super.onResume();
               mFirebseAuth.addAuthStateListener(mAuthStateListener);
           }
           @Override
           protected void onPause() {
               super.onPause();
               if(mAuthStateListener!=null){

                   mFirebseAuth.removeAuthStateListener(mAuthStateListener);
               }
           }


           private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Current_Location uInfo = ds.getValue(Current_Location.class);

            //display all the information
            Log.d(TAG, "showData: name: " + uInfo.getLatitude());
            Log.d(TAG, "showData: email: " + uInfo.getLongitude());
            Log.d(TAG, "showData: phone_num: " + uInfo.getMail_id());

            arraystore.add(uInfo.getMail_id());
            arraystore.add(uInfo.getLatitude());
            arraystore.add(uInfo.getLongitude());

              Current_long=new ArrayList<>();
            Current_long.add(new Current_Location(uInfo.getLongitude(),uInfo.getLatitude()));
            Current_Location location=Current_long.get(0);
            String lon=location.getLongitude();
            String lat=location.getLatitude();
            Log.v("MainActivity",lat+"  "+lon);


            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arraystore);
            mListView.setAdapter(adapter);
        }


    }



    public  static  LatLng getCurrentLat_Long(){

        Current_Location location=Current_long.get(0);
        LatLng latLng=new LatLng(Double.parseDouble(location.getLatitude())
                ,Double.parseDouble(location.getLongitude()));
        return latLng;
       /* ArrayList<HashMap<Double,Double>>Location_double=new ArrayList<>();
        HashMap<Double,Double> latlngDou=new HashMap<>();
        latlngDou.put(Double.parseDouble(location.getLatitude()),Double.parseDouble(location.getLongitude()));
        Location_double.add(latlngDou);
        return  Location_double;
*/
    }

    public  static LatLng getPostion(){

       LatLng k=new LatLng(mlatitude,mlongitude);
       return  k;


    }



           @Override
           public boolean onCreateOptionsMenu(Menu menu) {
               MenuInflater inflater=getMenuInflater();
               inflater.inflate(R.menu.main_menu,menu);
               return true;
           }

           @Override
           public boolean onOptionsItemSelected(MenuItem item) {
               switch (item.getItemId()){
                   case R.id.sign_out_menu:
                       AuthUI.getInstance().signOut(MainActivity.this);
                       return true;

                   case R.id.mapfragment:
                       Intent intent=new Intent(this, MapActivity.class);
                       startActivity(intent);
                   default:
                       return super.onOptionsItemSelected(item);
               }


           }




           @SuppressWarnings("StatementWithEmptyBody")
           @Override
           public boolean onNavigationItemSelected(MenuItem item) {
               // Handle navigation view item clicks here.
               int id = item.getItemId();

               if (id == R.id.nav_camera) {
                   // Handle the camera action
               } else if (id == R.id.nav_gallery) {

               } else if (id == R.id.nav_slideshow) {

               } else if (id == R.id.nav_manage) {

               } else if (id == R.id.nav_share) {

               } else if (id == R.id.nav_send) {

               }

               DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
               drawer.closeDrawer(GravityCompat.START);
               return true;
           }

           @Override
           public void onConnected(@Nullable Bundle bundle) {

               mLocationRequest = LocationRequest.create();
               mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
               mLocationRequest.setInterval(100);
               if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                   ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                   return;

                   // TODO: Consider calling
                   //    ActivityCompat#requestPermissions
                   // here to request the missing permissions, and then overriding
                   //public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                   //                                 int[] grantResults){

                   // }

                   // to handle the case where the user grants the permission. See the documentation
                   // for ActivityCompat#requestPermissions for more details.

               }
               LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


           }

           @Override
           public void onConnectionSuspended(int i) {

           }

           @Override
           public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

           }

           @Override
           public void onMapReady(GoogleMap googleMap) {

           }

           @Override
           public void onLocationChanged(Location location) {
         mlatitude=location.getLatitude();
         mlongitude=location.getLongitude();


           }

           @Override
           protected void onStart() {
        mGoogleApiClient.connect();
               super.onStart();
           }

           @Override
           protected void onStop() {
               mGoogleApiClient.disconnect();
               super.onStop();
           }
       }
