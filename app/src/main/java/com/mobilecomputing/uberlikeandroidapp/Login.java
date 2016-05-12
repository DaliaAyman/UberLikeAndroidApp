package com.mobilecomputing.uberlikeandroidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Login extends AppCompatActivity {

    EditText username;
    EditText password;
    Button ok;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("Uber", 0);
        editor = sharedPreferences.edit();

        username = (EditText)findViewById(R.id.username_login);
        password = (EditText)findViewById(R.id.password_login);
        ok = (Button)findViewById(R.id.ok_login);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String pass = password.getText().toString();

                if(!userName.isEmpty() && !pass.isEmpty()) {
                    new LoginTask(userName, pass).execute();
                }
            }
        });
    }

    class LoginTask extends AsyncTask<String, String, String> {

        String mail, pass, urlPath;
        StringBuilder response;

        LoginTask(String mail, String pass) {
            this.mail = mail;
            this.pass = pass;
            urlPath = "http://uberlikeapp-ad3rhy2.rhcloud.com/api/user/login";
        }
        @Override
        protected String doInBackground(String... params) {

            if(isNetworkAvailable()) {
                if(isOnline()) {
                    URL url = null;
                    try {
                        url = new URL(urlPath);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        urlConnection.setRequestMethod("POST");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    urlConnection.setRequestProperty("password", pass);
                    urlConnection.setRequestProperty("email", mail);

                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    try {
                        urlConnection.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InputStream in = null;
                    try {
                        in = urlConnection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not online", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            editor.putBoolean("logged_in", true);
            editor.commit();
            Toast.makeText(getApplicationContext(), response.toString() + "               Hello from ahmed", Toast.LENGTH_LONG).show();
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
