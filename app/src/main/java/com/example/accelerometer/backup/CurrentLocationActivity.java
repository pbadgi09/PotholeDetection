package com.example.accelerometer.backup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accelerometer.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class CurrentLocationActivity extends AppCompatActivity implements LocationListener {

    private  String TAG = "shreyash";
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latTextView, lonTextView;
     Context context = this;
     Handler handler = new Handler();
     Button start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        Log.d(TAG,"inside OnCreate");
        latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);
        start_btn=(Button)findViewById(R.id.bt_start);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();



       start_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SmartLocation.with(context).location().start(onLocationUpdatedListener);
           }
       });






    }
    private OnLocationUpdatedListener onLocationUpdatedListener = new OnLocationUpdatedListener() {
        @Override
        public void onLocationUpdated(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            handler.postDelayed(locationRunnable,8000);
            latTextView.setText(""+location.getLatitude());
            lonTextView.setText(""+location.getLongitude());

            Snackbar snackbar = Snackbar
                    .make(latTextView, "lattitude:"+location.getLatitude()+" longitude:"+location.getLongitude(), Snackbar.LENGTH_LONG);

            snackbar.show();
            Log.d(TAG,"onLocationUpdatedListener lattitude:"+location.getLongitude()+" longitude:"+location.getLongitude());
        }
    };


    private Runnable locationRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG,"run()");
            SmartLocation.with(context).location().start(onLocationUpdatedListener);
        }
    };

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        Log.d(TAG,"getLastLocation()");

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latTextView.setText(location.getLatitude()+"");
                                    lonTextView.setText(location.getLongitude()+"");
                                    Snackbar snackbar = Snackbar
                                            .make(latTextView, "getLastLocation()", Snackbar.LENGTH_SHORT);

                                    snackbar.show();
                                    Log.d(TAG,"getLastLocation() lattitude:"+location.getLongitude()+" longitude:"+location.getLongitude());

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){
        Log.d(TAG,"requestNewLocationData()");


        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView.setText(mLastLocation.getLatitude()+"");
            lonTextView.setText(mLastLocation.getLongitude()+"");
            Snackbar snackbar = Snackbar
                    .make(latTextView, "LocationCallBack()", Snackbar.LENGTH_SHORT);

            snackbar.show();
            Log.d(TAG,"LocationCallBack() lattitude:"+mLastLocation.getLatitude()+" longitude:"+mLastLocation.getLongitude());
        }
    };

    //is permission request granted or not
    private boolean checkPermissions() {
        Log.d(TAG,"checkPermission");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"checkPermission PERMISSION_GRANTED");

            return true;
        }

        return false;
    }
    private void requestPermissions() {
        Log.d(TAG,"requestPermission()");

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    //is location enabled from setting
    private boolean isLocationEnabled() {
        Log.d(TAG,"isLocationEnabled()");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"OnRequestPermissionResult() PERMISSION_GRANTED");

                getLastLocation();
            }
        }
    }

    /*@Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }*/



    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"onLocationChanged");
        latTextView.setText(""+location.getLatitude());
        lonTextView.setText(""+location.getLongitude());
        Snackbar snackbar = Snackbar
                .make(latTextView, "onLocationChanged()", Snackbar.LENGTH_LONG);

        snackbar.show();
        latTextView.setTextColor(Color.GREEN);
        lonTextView.setTextColor(Color.GREEN);


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG,"ProviderEnabled()");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG,"ProviderDisabled()");
    }

    @Override
    public void onStop() {
        SmartLocation.with(context).location().stop();
        super.onStop();
    }
}
