package com.example.distancecount;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnShowLocation;
    TextView tv_show;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;

    Context context;

    double latitude;
    double longitude;
    Location start_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getBaseContext();
        tv_show = (TextView) findViewById(R.id.tv_show);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //  execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation = (Button) findViewById(R.id.button);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    start_location = new Location("start point");
                    start_location.setLatitude(latitude);
                    start_location.setLongitude(longitude);

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude + "start_location" + start_location, Toast.LENGTH_LONG).show();
                    sharesPreferanceSave();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }

    public void sharesPreferanceSave() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_gps), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_start_distance), start_location.toString());
        editor.commit();
    }

    public void sharesPreferanceShow(View view) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_gps), Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.saved_start_distance);
        String highScore = sharedPref.getString(getString(R.string.saved_start_distance), defaultValue);

        tv_show.setText(highScore);

    }
}
