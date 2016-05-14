package com.mobilecomputing.uberlikeandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RequestRideActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mapLite;
    private static final int RC_DROP_OFF = 9100;
    private Button confirmRide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.request_ride_lite_mode_map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);

        confirmRide = (Button) findViewById(R.id.request_ride_confirm_ride);
        confirmRide.setEnabled(false);

        Button addDropOffButton = (Button) findViewById(R.id.request_ride_add_dropoff_button);
        addDropOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestRideActivity.this, AddDropOffActivity.class);
                startActivityForResult(intent, 9100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_DROP_OFF){
            if (resultCode == RESULT_OK){
             confirmRide.setEnabled(true);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapLite = googleMap;
        LatLng fromLocation = new LatLng(-34, 141);
        mapLite.addMarker(new MarkerOptions().position(fromLocation).title("My pickup location"));
    }
}
