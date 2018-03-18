package com.example.android.policetrackingapp;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import android.widget.ProgressBar;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.android.policetrackingapp.Data.DetailsContract;
import com.example.android.policetrackingapp.Data.PersonDetailsDBHelper;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

public class MainActivity extends AppCompatActivity
       implements NavigationView.OnNavigationItemSelectedListener
        ,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener,DataDisplayAdapter.DataDisplayAdapterOnClickHandler

{

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private SQLiteDatabase mDb;
    private Cursor mCursor;
    private static FirebaseUser user;
    private String Phone_no;
    private static String pAddress;
    private Police_data policedata;
   private  FirebaseAuth Auth;
    private static final String TAG = "ViewDatabase";

    private static ArrayList<Current_Location> arrayList;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;

    private static List<Current_Location> Current_long;
    private DatabaseReference myRef;
    private String UserId;
    private ListView mListView;
    private static ArrayList<String> arraystore;
    private FirebaseAuth mFirebseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    int count = 0;
    static double mlatitude;
    static double mlongitude;
    static double mlatitude_tar;
    static double mlongitude_tar;
    private static final int LOCATION_REQUEST = 500;
    private static final int ID_FORECAST_LOADER = 44;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private RecyclerView mRecyclerView;
    private DataDisplayAdapter madapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mLoadingIndicator;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private Query query;
    private  SwipeRefreshLayout refreshListener;
    private AsyncTask Backtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshListener=findViewById(R.id.swiperefresh);


        mFirebseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    UserId = user.getEmail();
                    String compare1 = getForm(UserId);

                    if (compare1.equals("")) {

                        Intent intent = new Intent(MainActivity.this, FormActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "You're now signed in. Welcome to My Safety app .", Toast.LENGTH_SHORT).show();
                        Display_data();
                        QueryAddress();

                    }
                } else {
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





        myRef = mFirebaseDatabase.getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        madapter = new DataDisplayAdapter(this, this);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        Auth=FirebaseAuth.getInstance();
        user=Auth.getCurrentUser();

        refreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


                if (networkInfo != null && networkInfo.isConnected()) {
                   finish();
                    startActivity(getIntent());


                } else {
                    Toast.makeText(MainActivity.this, "Make sure that your Device is connected to internet", Toast.LENGTH_LONG).show();
                }

                refreshListener.setRefreshing(false);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);


                    return;
                }
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
        //Display_data();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {

            mFirebseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }



    public static LatLng getCurrentLat_Long() {

        LatLng latLng = new LatLng(mlatitude_tar
                , mlongitude_tar);
        return latLng;


    }

    public static LatLng getPostion() {

        LatLng k = new LatLng(mlatitude, mlongitude);
        return k;


    }

    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(MainActivity.this);
                return true;

            case R.id.mapfragment:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }


    }





    private String getForm(String UserId) {
        PersonDetailsDBHelper dbHelper = new PersonDetailsDBHelper(MainActivity.this);
        mDb = dbHelper.getReadableDatabase();
        String compare = "";
        mCursor = mDb.query(DetailsContract.DetailsEntry.TABLE_NAME, new String[]{DetailsContract.DetailsEntry.EMAIL_ADD, DetailsContract.DetailsEntry.PHONE_NO},
                DetailsContract.DetailsEntry.EMAIL_ADD + " = ? ", new String[]{UserId}, null, null, null);
        if (mCursor.getCount() > 0) {
            mCursor.moveToFirst();

            compare = mCursor.getString((mCursor.getColumnIndex(DetailsContract.DetailsEntry.EMAIL_ADD)));
            Phone_no = mCursor.getString(mCursor.getColumnIndex(DetailsContract.DetailsEntry.PHONE_NO));
            return compare;
        } else {

            return compare;
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
        mlatitude = location.getLatitude();
        mlongitude = location.getLongitude();


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


    @Override
    public synchronized void onClick(Current_Location location) {
        LatLng latLng = new LatLng(Double.parseDouble(location.getLatitude())
                , Double.parseDouble(location.getLongitude()));
        mlatitude_tar = latLng.latitude;
        mlongitude_tar = latLng.longitude;
        String uid = location.getMuid();
        String status = location.getMstatus();

        String compare = "Not Accepted";
        if (status.equals(compare)) {




            String new_status = "Request is Accepted by " + user.getDisplayName()+" " + "From  " + pAddress;
            String message="Your Request is accepted by Police officer "+user.getDisplayName()+" From "+pAddress;
            location.setMstatus(new_status);
            mFirebaseDatabase.getReference().child("DATA").child(uid).setValue(location);
            Smsutilites.sendmessage(this,message,location.getMphone_no());
            Toast.makeText(getApplicationContext(), "Location is set", Toast.LENGTH_LONG).show();


        } else {

            Toast.makeText(getApplicationContext(), "Not Called", Toast.LENGTH_LONG).show();

        }
    }


    public static FirebaseUser getUserdetails() {


        return user;

    }

    private void Display_data() {
        myRef.child("DATA").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList = new ArrayList<Current_Location>();
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Current_Location uInfo = ds.getValue(Current_Location.class);
                    arrayList.add(uInfo);

                    i++;
                }
                madapter.setUserdata(arrayList);
                mRecyclerView.setAdapter(madapter);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                int size = arrayList.size();
                Current_Location location = arrayList.get(size - 1);
                mlatitude_tar = Double.parseDouble(location.getLatitude());
                mlongitude_tar = Double.parseDouble(location.getLongitude());

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });




    }

    public  void QueryAddress(){

        query = mFirebaseDatabase.getReference().child("POLICE_DATA").orderByChild("memail").equalTo(user.getEmail());

        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                policedata = (Police_data) dataSnapshot.getValue(Police_data.class);
                pAddress=policedata.maddress;


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }






}
