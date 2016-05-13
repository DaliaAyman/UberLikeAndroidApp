package com.mobilecomputing.uberlikeandroidapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobilecomputing.uberlikeandroidapp.DataModels.Driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final int  MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest request;
    private GoogleMap mMap;
    private LatLng currentLocation;
    private LatLng requestLocation;

    BroadcastReceiver driverLocationUpdatesReceiver;

    HashMap<String, Driver> currentDrivers;

    Button signUp;
    Button login;
    Button requestRide;
    private double lat;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentDrivers = new HashMap();

        driverLocationUpdatesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Updates data", intent.getExtras().toString());
                Toast.makeText(getApplicationContext(), intent.getExtras().toString(), Toast.LENGTH_SHORT).show();

            }
        };
        registerReceiver(driverLocationUpdatesReceiver, new IntentFilter("Data_GCM"));

        boolean Services_available = checkGooglePlayServices(this);
        if (Services_available) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        currentLocation = new LatLng(0,0);
        requestLocation = new LatLng(10,10);

        signUp = (Button)findViewById(R.id.signupButton);
        login = (Button)findViewById(R.id.login);
        requestRide = (Button)findViewById(R.id.requestRideBtn);

        SharedPreferences sharedPreferences = getSharedPreferences("Uber", 0);

        if(sharedPreferences.getBoolean("registered", false)) {
            signUp.setVisibility(View.GONE);
            if(sharedPreferences.getBoolean("logged_in", false)) {
                login.setVisibility(View.GONE);
            }
            else {
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent loginActivity = new Intent(MainActivity.this, Login.class);
                        startActivity(loginActivity);
                    }
                });
                requestRide.setVisibility(View.GONE);
            }

        }
        else {
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signup = new Intent(MainActivity.this, Signup.class);
                    startActivity(signup);
                }
            });
            requestRide.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));

        mMap.addMarker(new MarkerOptions()
                .position(requestLocation)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        //mMap.setMyLocationEnabled(true);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    //This method is invoked after requestLocationUpdates
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mLastLocation != null) {
            // Toast.makeText(this, "Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();

            mLastLocation = location;

            if (mLastLocation != null) {
                Toast.makeText(this, "Latitude:" + mLastLocation.getLatitude() + ", Longitude:" + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
            }
            double lastLat = 0;
            int latEqual = Double.compare(mLastLocation.getLatitude(), lastLat);
            double lastLong = 0;
            int LongEqual = Double.compare(mLastLocation.getLongitude(), lastLong);
            if ((mLastLocation.getLatitude() - lastLat == 0.005) && (mLastLocation.getLongitude() - lastLong == 0.0005)) {
                lastLat = mLastLocation.getLatitude();
                lastLong = mLastLocation.getLongitude();
                this.lat = mLastLocation.getLatitude();
                this.longitude = mLastLocation.getLongitude();
                PutLocation test = new PutLocation();
                test.execute();

            }
        }
    }

    //Create Location requests to periodically request a location update
    protected void createLocationRequest() {
        request = new LocationRequest();
        request.setInterval(20000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:{
                //The application granted the permission
                startLocationUpdates();
            }

        }
    }


    public boolean checkGooglePlayServices(Context mContext){
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        AlertDialog unavailable_play_services=new AlertDialog.Builder(this).create();
        unavailable_play_services.setTitle("Google Play services are not installed");
        unavailable_play_services.setMessage("Please Install Google Play Services");
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                unavailable_play_services.show();
            }

            return false;
        }

        return true;
    }


    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        requestLocation = marker.getPosition();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class PutLocation extends AsyncTask <Void,Void,Void>{
        private static final String ID = "id";
        String Url="http://uberlikeapp-ad3rhy2.rhcloud.com//api/user/updateUserLocation";
        StringBuilder stringBuilder;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getBaseContext(),stringBuilder,Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            try{

                URL url = new URL(Url);
                HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("PUT");
                String type = "oo";
                urlConnection.setRequestProperty("type", type);
                urlConnection.setRequestProperty("driver_id", ID);
                urlConnection.setRequestProperty("lat", String.valueOf(lat));
                urlConnection.setRequestProperty("lng", String.valueOf(longitude));
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                InputStream in=urlConnection.getInputStream();
                stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }




            }
           catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{}
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(driverLocationUpdatesReceiver);
    }
}
