package com.gorillas.oxyapp;


import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    }

    private void Start() {
        Log.d("TAG", "Phase1-------------------------------");
        CountDownTimer count =  new CountDownTimer(20000, 1000) {
            int basehr = 65;
            int baseoxy = 98;
            @Override
            public void onTick(long millisUntilFinished) {

                Random num = new Random();
                hr= basehr + (num.nextInt(5));
                oxy= baseoxy + (num.nextInt(3));

                //Log.d("TAG", "hr= " + hr);
                // Log.d("TAG", "oxy= " + oxy);

                if ((hr<= 70 || oxy> 90) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr<= 130 || oxy> 80) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr<= 200 || oxy<= 80) && !mPause) {
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
                if(basehr % 2 == 0) --baseoxy;
            }

            @Override
            public void onFinish() {
                Phase2(basehr,baseoxy);

            }
        }.start();
    }

    private void Phase2(final int basehr2, final int baseoxy2) {
        Log.d("TAG", "Phase2-------------------------------");
        CountDownTimer count = new CountDownTimer(20000, 1000) {
            int baseoxy = baseoxy2;
            int basehr = basehr2;
            @Override
            public void onTick(long millisUntilFinished) {

                Random num = new Random();
                hr= basehr + (num.nextInt(5));
                oxy= baseoxy + (num.nextInt(3));

                // Log.d("TAG", "hr= " + hr);
                // Log.d("TAG", "oxy= " + oxy);

                if ((hr<= 70 || oxy> 90) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr<= 130 && oxy> 80) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr<= 2000 || oxy<= 800) && !mPause) {
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
                if(basehr % 3 != 0) --baseoxy;
            }

            @Override
            public void onFinish() {
                Phase3(basehr,baseoxy);

            }
        }.start();

    }

    private void Phase3(final int basehr2, final int baseoxy2) {
        Log.d("TAG", "Phase3-------------------------------");
        CountDownTimer count = new CountDownTimer(200000, 1000) {
            int baseoxy = baseoxy2;
            int basehr = basehr2;
            @Override
            public void onTick(long millisUntilFinished) {

                Random num = new Random();
                hr= basehr + (num.nextInt(5));
                oxy= baseoxy + (num.nextInt(3));

                // Log.d("TAG", "hr= " + hr);
                // Log.d("TAG", "oxy= " + oxy);

                if ((hr<= 70 || oxy> 90) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr<= 130 && oxy> 80) && !mPause) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                    //if it's vibrating, i cancel it
                    mVibrator.cancel();
                } else if ((hr<= 2000 || oxy<= 800) && !mPause) {
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
                if(basehr > 65) basehr -= 2;
                if(basehr % 2 == 0 && baseoxy < 98) ++baseoxy;
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
            if(mVibrator.hasVibrator()) mVibrator.cancel();
        }
    }
}