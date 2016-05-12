package com.mobilecomputing.uberlikeandroidapp.DataModels;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by AhmedA on 5/2/2016.
 */
public class Client {

    private String fullName;
    private String mobile;
    private String password;
    private String email;
    private static String type = "client";
    private String reg_id;

    private LatLng currentLocation;
    private ArrayList<Ride> userRides;

    public Client(String fullName, String mobile, String password, String email, String reg_id, LatLng currentLocation, ArrayList<Ride> userRides) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.password = password;
        this.email = email;
        this.reg_id = reg_id;
        this.currentLocation = currentLocation;
        this.userRides = userRides;
    }

    public Client() {
        userRides = new ArrayList<>();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        Client.type = type;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public ArrayList<Ride> getUserRides() {
        return userRides;
    }

    public void setUserRides(ArrayList<Ride> userRides) {
        this.userRides = userRides;
    }
}