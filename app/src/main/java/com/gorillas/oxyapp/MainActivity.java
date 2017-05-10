package com.gorillas.oxyapp;


import android.app.Dialog;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.zzl;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ImageView mImageStatus;
    private TextView mTextStatus;
    private TextView mTextPulse;
    private TextView mTextSpo2;
    private ImageView mImageStart;
    private ImageView mImagePause;
    private ImageView mImageStop;
    private int hr;
    private int oxy;
    private Vibrator mVibrator;
    private boolean mPause;


    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;

    public boolean googleServicesAvariable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to  play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageStatus = (ImageView) findViewById(R.id.imageView_status);
        mTextStatus = (TextView) findViewById(R.id.text_status);
        mTextPulse = (TextView) findViewById(R.id.pulse_number);
        mTextSpo2 = (TextView) findViewById(R.id.spo2_number);

        mImageStart = (ImageView) findViewById(R.id.imageView_play);
        mImageStart.setOnClickListener(this);
        mImagePause = (ImageView) findViewById(R.id.imageView_pause);
        mImagePause.setOnClickListener(this);
        mImageStop = (ImageView) findViewById(R.id.imageView_stop);
        mImageStop.setOnClickListener(this);
        mPause = false;
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (googleServicesAvariable()) {
            Toast.makeText(this, "Perfect", Toast.LENGTH_LONG).show();
            initMap();

        }


    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }


    private CountDownTimer count;

    private void Start() {
        Log.d("TAG", "Phase1-------------------------------");
        count = new CountDownTimer(20000, 1000) {
            int basehr = 65;
            int baseoxy = 98;

            @Override
            public void onTick(long millisUntilFinished) {

                Random num = new Random();
                hr = basehr + (num.nextInt(5));
                oxy = baseoxy + (num.nextInt(3));

                //Log.d("TAG", "hr= " + hr);
                // Log.d("TAG", "oxy= " + oxy);

                if ((hr <= 70 || oxy > 90) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr <= 130 || oxy > 80) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr <= 200 || oxy <= 80) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.stop);
                    mTextStatus.setText(R.string.have_a_break);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    // Vibrate for 2 minuts
                    mVibrator.vibrate(120000);
                } else {
                    //mTextPulse.setText("Error");
                    //if i'm here it's because i'm in pause or stop
                }
                ++basehr;
                if (basehr % 2 == 0) --baseoxy;
            }

            @Override
            public void onFinish() {
                Phase2(basehr, baseoxy);

            }
        }.start();
    }

    private void Phase2(final int basehr2, final int baseoxy2) {
        Log.d("TAG", "Phase2-------------------------------");
        count = new CountDownTimer(20000, 1000) {
            int baseoxy = baseoxy2;
            int basehr = basehr2;

            @Override
            public void onTick(long millisUntilFinished) {

                Random num = new Random();
                hr = basehr + (num.nextInt(5));
                oxy = baseoxy + (num.nextInt(3));

                // Log.d("TAG", "hr= " + hr);
                // Log.d("TAG", "oxy= " + oxy);

                if ((hr <= 70 || oxy > 90) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr <= 130 && oxy > 80) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr <= 2000 || oxy <= 800) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.stop);
                    mTextStatus.setText(R.string.have_a_break);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    // Vibrate for 2 minuts
                    mVibrator.vibrate(120000);
                } else {
                    //mTextPulse.setText("Error");
                    //if i'm here it's because i'm in pause or stop
                }
                basehr += 3;
                if (basehr % 3 != 0) --baseoxy;
            }

            @Override
            public void onFinish() {
                Phase3(basehr, baseoxy);

            }
        }.start();

    }

    private void Phase3(final int basehr2, final int baseoxy2) {
        Log.d("TAG", "Phase3-------------------------------");
        count = new CountDownTimer(200000, 1000) {
            int baseoxy = baseoxy2;
            int basehr = basehr2;

            @Override
            public void onTick(long millisUntilFinished) {

                Random num = new Random();
                hr = basehr + (num.nextInt(5));
                oxy = baseoxy + (num.nextInt(3));

                // Log.d("TAG", "hr= " + hr);
                // Log.d("TAG", "oxy= " + oxy);

                if ((hr <= 70 || oxy > 90) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr <= 130 && oxy > 80) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr <= 2000 || oxy <= 800) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.stop);
                    mTextStatus.setText(R.string.have_a_break);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    // Vibrate for 2 minuts
                    mVibrator.vibrate(120000);
                } else {
                    //mTextPulse.setText("Error");
                    //if i'm here it's because i'm in pause or stop
                }
                if (basehr > 65) basehr -= 2;
                if (basehr % 2 == 0 && baseoxy < 98) ++baseoxy;
            }

            @Override
            public void onFinish() {
                Log.d("TAG", "Simulation Finished -------------------------------");

            }
        }.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.show_maps:
                Log.d("MainActivity", "map selected");
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mImageStart) {
            Start();
            mPause = false;
            mImageStart.setImageResource(R.drawable.ic_play_green);
            mImagePause.setImageResource(R.drawable.ic_pause);
            mImageStop.setImageResource(R.drawable.ic_stop);
        } else if (v == mImagePause) {
            mPause = true;
            mImageStart.setImageResource(R.drawable.ic_play);
            mImagePause.setImageResource(R.drawable.ic_pause_blue);
            mImageStop.setImageResource(R.drawable.ic_stop);
        } else if (v == mImageStop) {
            mPause = true;
            mImageStart.setImageResource(R.drawable.ic_play);
            mImagePause.setImageResource(R.drawable.ic_pause);
            mImageStop.setImageResource(R.drawable.ic_stop_red);
            //we have to restart the variables
            mTextPulse.setText("0");
            mTextSpo2.setText("0");
            if (mVibrator.hasVibrator()) mVibrator.cancel();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



        mGoogleApiClient.connect();
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspend!!!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "connection failed!!!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Location change!!!", Toast.LENGTH_LONG).show();
        if (location == null) {
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "current location Found!!!", Toast.LENGTH_LONG).show();
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,10);
            mGoogleMap.animateCamera(update);


            MarkerOptions options = new MarkerOptions()
                            .title("my position")
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_dot))
                            .position(ll);
            mGoogleMap.addMarker(options);
        }

    }
}