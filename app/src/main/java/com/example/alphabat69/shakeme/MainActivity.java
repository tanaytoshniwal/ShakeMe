package com.example.alphabat69.shakeme;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    //private VideoView bod;
    private ImageView bod;
    private TextView sensitivity;
    private SeekBar seekBar;
    private float SHAKE_THRESHOLD = 2.5f; //max 5.0
    private SensorManager mSensorMgr;
    private int countpause=0;
    private MediaPlayer mp3;
    final float MAX= 10.0f;
    float f=1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        bod=findViewById(R.id.bod);
        sensitivity=findViewById(R.id.sensitivity);
        seekBar=findViewById(R.id.seekBar);
        //String path="android.resource://com.example.alphabat69.shakeme/"+R.raw.trim;
        //Uri uri=Uri.parse(path);
        //bod.setVideoURI(uri);
        Glide.with(this).asGif().load(R.raw.batman).into(bod);
        bod.setVisibility(View.INVISIBLE);
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        mp3=MediaPlayer.create(MainActivity.this,R.raw.drugs2);
        mp3.setLooping(true);
        mp3.setVolume(1.0f,1.0f);
        register();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sensitivity.setText("Sensitivity("+i+")");
                SHAKE_THRESHOLD=MAX-f*i;
                //Toast.makeText(MainActivity.this, ""+SHAKE_THRESHOLD, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    public void register(){
        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double acceleration = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
            Log.d("tag", "Acceleration is " + acceleration + "m/s^2");
            if (acceleration > SHAKE_THRESHOLD) {
                //play video
                //if (!bod.isPlaying())
                //    bod.start();
                bod.setVisibility(View.VISIBLE);
                mp3.start();
            }else {
                //pause video
                countpause++;
                if(countpause>2&&SHAKE_THRESHOLD!=0.0f) {
                    //bod.pause();
                    bod.setVisibility(View.INVISIBLE);
                    if(mp3.isPlaying()) {
                        mp3.pause();
                        mp3.seekTo(mp3.getCurrentPosition());
                    }
                    countpause=0;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        register();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp3.pause();
        mSensorMgr.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
