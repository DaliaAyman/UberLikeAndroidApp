package com.mobilecomputing.uberlikeandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobilecomputing.uberlikeandroidapp.Utilities.Global;

public class AddDropOffActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;
    private LatLng dropOffLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drop_off);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.drop_off_map);
        mapFragment.getMapAsync(this);
        dropOffLocation = new LatLng(10,10);

        Button drop_off_button = (Button) findViewById(R.id.confirm_dropoff);
        drop_off_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnedIntent = new Intent();
                returnedIntent.putExtra(Global.DROPOFF_LAT, dropOffLocation.latitude);
                returnedIntent.putExtra(Global.DROPOFF_LON, dropOffLocation.longitude);
                setResult(Activity.RESULT_OK, returnedIntent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(dropOffLocation).title("Current Location"));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
