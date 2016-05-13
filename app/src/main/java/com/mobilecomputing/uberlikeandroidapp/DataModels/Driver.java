package com.mobilecomputing.uberlikeandroidapp.DataModels;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AhmedA on 5/13/2016.
 */
public class Driver {
    String driverID;
    LatLng currentLocation;

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setCurrentLocation(double lat, double lng) {
        this.currentLocation = new LatLng(lat, lng);
    }
}
