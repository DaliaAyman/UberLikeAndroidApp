package com.mobilecomputing.uberlikeandroidapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrackDriverActivity extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mMap;
    LatLng clientLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_driver);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.driver_map);
        mapFragment.getMapAsync(this);

        //static data
        clientLocation = new LatLng(30.065136, 31.278821);

    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        mMap.addMarker(new MarkerOptions()
                .position(clientLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(clientLocation)      // Sets the center of the map to Client
                .zoom(17)
                .build();                   // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
