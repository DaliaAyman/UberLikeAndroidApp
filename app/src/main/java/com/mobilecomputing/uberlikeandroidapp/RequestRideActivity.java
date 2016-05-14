package com.mobilecomputing.uberlikeandroidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobilecomputing.uberlikeandroidapp.Utilities.Global;
import com.mobilecomputing.uberlikeandroidapp.Utilities.RequestRideParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestRideActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapLite;
    private static final int RC_DROP_OFF = 9100;
    private Button confirmRide;
    private ProgressDialog mProgressDialog;
    private SharedPreferences sharedPreferences;
    RequestRideParams requestRideParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.request_ride_lite_mode_map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);

        confirmRide = (Button) findViewById(R.id.request_ride_confirm_ride);
        confirmRide.setEnabled(false);
        sharedPreferences = this.getSharedPreferences("Uber", MODE_PRIVATE);
        requestRideParams = new RequestRideParams();

        requestRideParams.setClient_id(sharedPreferences.getString("client_id", ""));

        Button addDropOffButton = (Button) findViewById(R.id.request_ride_add_dropoff_button);
        addDropOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestRideActivity.this, AddDropOffActivity.class);
                startActivityForResult(intent, RC_DROP_OFF);
            }
        });

        Intent intent = getIntent();
        requestRideParams.setPick_up_lat(intent.getDoubleExtra(Global.PICKUP_LAT, 0));
        requestRideParams.setPick_up_lon(intent.getDoubleExtra(Global.PICKUP_LON, 0));

        confirmRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestRideToServer();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_DROP_OFF) {
            if (resultCode == RESULT_OK) {
                requestRideParams.setDrop_off_lat(data.getDoubleExtra(Global.DROPOFF_LAT, 0));
                requestRideParams.setDrop_off_lon(data.getDoubleExtra(Global.DROPOFF_LON, 0));
                confirmRide.setEnabled(true);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapLite = googleMap;
        LatLng fromLocation = new LatLng(requestRideParams.getPick_up_lat(), requestRideParams.getPick_up_lon());
        mapLite.addMarker(new MarkerOptions().position(fromLocation).title("My pickup location"));
    }


    private void sendRequestRideToServer() {
        mProgressDialog = new ProgressDialog(RequestRideActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Requesting ride...");
        mProgressDialog.show();
        //new PostRequestRide().execute(requestRideParams);
    }

    private class PostRequestRide extends AsyncTask<RequestRideParams, Void, Void> {
        URL url;
        HttpURLConnection connection;

        @Override
        protected Void doInBackground(RequestRideParams... params) {
            try {
                //url = new URL(Global.SERVER_URL + "/token/google");
                connection = (HttpURLConnection) url.openConnection();

                // set connection to allow output
                connection.setDoOutput(true);

                // set connection to allow input
                connection.setDoInput(true);

                // set the request method to POST
                connection.setRequestMethod("POST");

                // set content-type property
                connection.setRequestProperty("Content-Type", "application/json");

                // set charset property to utf-8
                connection.setRequestProperty("charset", "utf-8");

                // put user name and id token in a JSONObject
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("client_id", params[0].getClient_id());
                jsonBody.put("driver_id", params[0].getDriver_id());
                jsonBody.put("from_lat", params[0].getPick_up_lat());
                jsonBody.put("from_lon", params[0].getPick_up_lon());
                jsonBody.put("to_lat", params[0].getDrop_off_lat());
                jsonBody.put("to_lon", params[0].getDrop_off_lon());

                // connect to server
                connection.connect();

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                // write JSON body to the output stream
                outputStream.write(jsonBody.toString().getBytes("utf-8"));

                // flush to ensure all data in the stream is sent
                outputStream.flush();

                // close stream
                outputStream.close();

                // receive the response from server
                //getResponseFromServer();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

