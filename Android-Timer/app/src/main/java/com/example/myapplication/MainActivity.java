package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewCountDown;
    private TextView mtextViewMin;
    private NumberPicker mTimeMin;
    private TextView mtextViewSec;
    private NumberPicker mTimeSec;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButtonSet;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimerLeftInMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.textViewCountDown);

        mtextViewMin = findViewById(R.id.textViewMin);
        mTimeMin = findViewById(R.id.TimeMin);

        mtextViewSec = findViewById(R.id.textViewSec);
        mTimeSec = findViewById(R.id.TimeSec);

        mTimeMin.setMinValue(0);
        mTimeMin.setMaxValue(60);

        mTimeSec.setMinValue(0);
        mTimeSec.setMaxValue(60);

        mButtonStartPause = findViewById(R.id.button_start);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonSet = findViewById(R.id.button_set);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", String.format("%02d:%02d",mTimeMin.getValue(),mTimeSec.getValue()));

                mTimerLeftInMS = (mTimeMin.getValue() * 1000) * 60 + mTimeSec.getValue() * 1000;
                if (mTimerLeftInMS != 0){
                    mTextViewCountDown.setText(String.format("%02d:%02d:00",mTimeMin.getValue(),mTimeSec.getValue()));
                    mButtonStartPause.setVisibility(View.VISIBLE);
                }
                else{
                    mButtonStartPause.setVisibility(View.INVISIBLE);
                }

            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    pauseTimer();
                }
                else{
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimerLeftInMS,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimerLeftInMS = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mTextViewCountDown.setText("00:00:00");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                mButtonSet.setVisibility(View.VISIBLE);

            }
        }.start();
        mTimerRunning= true;
        mButtonStartPause.setText("Pause");
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonSet.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
        mButtonSet.setVisibility(View.VISIBLE);
    }
    private void resetTimer(){
        mTimerLeftInMS = (mTimeMin.getValue() * 1000) * 60 + mTimeSec.getValue() * 1000;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateCountDownText(){
        int mins = (int) (mTimerLeftInMS / 1000) / 60;
        int seconds = (int) (mTimerLeftInMS / 1000) % 60;
        int ms = (int) (mTimerLeftInMS % 1000) / 10;
        String timeLeftFormatted = String.format("%02d:%02d:%02d", mins, seconds, ms);
        mTextViewCountDown.setText(timeLeftFormatted);
        if (mins == 0 && ms==0 && (seconds == 3 || seconds == 2 || seconds == 1)  ){
            final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.beep_01);
            mediaPlayer.start();
        }

    }

}