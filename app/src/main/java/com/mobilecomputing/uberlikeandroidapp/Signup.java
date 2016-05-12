package com.mobilecomputing.uberlikeandroidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.mobilecomputing.uberlikeandroidapp.DataModels.Client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Signup extends AppCompatActivity {

    EditText email, password, fullName, mobile;
    Button signUp;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    GoogleCloudMessaging gcm;

    private static Client client;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "uberApp";
    public static final String SENDER_ID = "UBERLIKEANDROIDAPP";

    ArrayList<EditText> focusViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText)findViewById(R.id.email_1);
        password = (EditText)findViewById(R.id.passord_1);
        fullName = (EditText)findViewById(R.id.fullName);
        mobile = (EditText)findViewById(R.id.mobile);

        pref = getSharedPreferences("Uber", 0);
        editor = pref.edit();

        signUp = (Button)findViewById(R.id.signup);

        focusViews = new ArrayList<>();

        client = new Client();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                String name = fullName.getText().toString();
                String mob = mobile.getText().toString();
                if(validateUserData(emailValue, passwordValue, name, mob)) {

                    editor.putString("Name",name);
                    editor.putString("Mobile", mob);
                    editor.putString("email", emailValue);
                    editor.apply();

                    client.setEmail(emailValue);
                    client.setFullName(name);
                    client.setPassword(passwordValue);
                    client.setMobile(mob);

                    if(checkPlayServices()) {
                        new RegisterGCM(gcm, getApplicationContext(), editor, client).execute();
                    }
                }
                else {
                    for (EditText e: focusViews) {
                        e.setError("Not valid entry");
                    }
                    focusViews.clear();
                    Toast.makeText(getApplicationContext(),"Not a valid data fix the problems and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateUserData(String emailValue, String passwordValue, String name, String mob) {
        Boolean valid = true;
        if(!emailValue.contains("@")) {
            focusViews.add(email);
            valid = false;
        }
        if (passwordValue.length() >= 10) {
            focusViews.add(password);
            valid = false;
        }
        if (!PhoneNumberUtils.isGlobalPhoneNumber(mob)) {
            focusViews.add(mobile);
            valid = false;
        }
        return valid;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public static Client getClient() {
        return client;
    }
    class RegisterGCM extends AsyncTask<String, String, String> {

        GoogleCloudMessaging gcm;
        Context context;
        String regid;
        SharedPreferences.Editor editor;
        Client client;

        RegisterGCM(GoogleCloudMessaging g, Context c, SharedPreferences.Editor e, Client cl) {
            gcm = g;
            context = c;
            editor = e;
            client = cl;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(Signup.SENDER_ID);
                    Log.e("RegId", regid);

                    editor.putString("REG_ID", regid);
                    editor.commit();
                    client.setReg_id(regid);
                }

                return  regid;

            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
                return "Fails";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    class Registration extends AsyncTask<String, String, String> {

        String urlPath = "http://uberlikeapp-ad3rhy2.rhcloud.com//api/user/signup";

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isNetworkAvailable()) {
                if (isOnline()) {
                    try {
                        URL url = new URL(urlPath);
                        HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private Boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }

        public boolean isOnline() {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return false;
        }
    }
}
