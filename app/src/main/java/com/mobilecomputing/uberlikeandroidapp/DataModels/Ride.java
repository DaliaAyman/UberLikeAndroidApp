package com.mobilecomputing.uberlikeandroidapp.DataModels;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by AhmedA on 5/2/2016.
 */
public class Ride {
    private LatLng startLocation;
    private LatLng endLocation;

    public Ride(LatLng startLocation, LatLng endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }
}
