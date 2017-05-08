package com.gorillas.oxyapp;


import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageStatus;
    private TextView mTextStatus;
    private TextView mTextPulse;
    private TextView mTextSpo2;
    private int hr;
    private int oxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageStatus = (ImageView) findViewById(R.id.imageView_status);
        mTextStatus = (TextView) findViewById(R.id.text_status);
        mTextPulse = (TextView) findViewById(R.id.pulse_number);
        mTextSpo2 = (TextView) findViewById(R.id.spo2_number);

        
        
        
        Start();


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

                Log.d("TAG", "hr= " + hr);
                Log.d("TAG", "oxy= " + oxy);

                if (hr<= 70 || oxy> 90) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else if (hr<= 130 || oxy> 80) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else if (hr<= 200 || oxy<= 80) {
                    mImageStatus.setImageResource(R.mipmap.stop);
                    mTextStatus.setText(R.string.have_a_break);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else {
                    mTextPulse.setText("Error");
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

                Log.d("TAG", "hr= " + hr);
                Log.d("TAG", "oxy= " + oxy);

                if (hr<= 70 || oxy> 90) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else if (hr<= 130 && oxy> 80) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else if (hr<= 2000 || oxy<= 800) {
                    mImageStatus.setImageResource(R.mipmap.stop);
                    mTextStatus.setText(R.string.have_a_break);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else {
                    mTextPulse.setText("Error");
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

                Log.d("TAG", "hr= " + hr);
                Log.d("TAG", "oxy= " + oxy);

                if (hr<= 70 || oxy> 90) {
                    mImageStatus.setImageResource(R.mipmap.green_smiley);
                    mTextStatus.setText(R.string.enjoy_riding);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else if (hr<= 130 && oxy> 80) {
                    mImageStatus.setImageResource(R.mipmap.flame);
                    mTextStatus.setText(R.string.slow_down);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else if (hr<= 2000 || oxy<= 800) {
                    mImageStatus.setImageResource(R.mipmap.stop);
                    mTextStatus.setText(R.string.have_a_break);
                    mTextPulse.setText(String.valueOf(hr));
                    mTextSpo2.setText(String.valueOf(oxy) + "%");
                } else {
                    mTextPulse.setText("Error");
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
}