package com.mobilecomputing.uberlikeandroidapp.Utilities;

/**
 * Created by Reem Hamdy on 5/14/2016.
 */
public class RequestRideParams {
    private double pick_up_lat;
    private double pick_up_lon;
    private double drop_off_lon;
    private double drop_off_lat;
    private String client_id;
    private String driver_id;

    public RequestRideParams() {
    }

    public void setPick_up_lat(double pick_up_lat) {
        this.pick_up_lat = pick_up_lat;
    }

    public void setPick_up_lon(double pick_up_lon) {
        this.pick_up_lon = pick_up_lon;
    }

    public void setDrop_off_lon(double drop_off_lon) {
        this.drop_off_lon = drop_off_lon;
    }

    public void setDrop_off_lat(double drop_off_lat) {
        this.drop_off_lat = drop_off_lat;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public double getPick_up_lat() {
        return pick_up_lat;
    }

    public double getPick_up_lon() {
        return pick_up_lon;
    }

    public double getDrop_off_lon() {
        return drop_off_lon;
    }

    public double getDrop_off_lat() {
        return drop_off_lat;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getDriver_id() {
        return driver_id;
    }
}
