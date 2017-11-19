package com.example.alphabat69.shakeme;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private VideoView bod;
    private TextView sensitivity;
    private SeekBar seekBar;
    private float SHAKE_THRESHOLD = 2.5f; //max 5.0
    private SensorManager mSensorMgr;
    private int countpause=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        bod=findViewById(R.id.bod);
        sensitivity=findViewById(R.id.sensitivity);
        seekBar=findViewById(R.id.seekBar);
        String path="android.resource://com.example.alphabat69.shakeme/"+R.raw.trim;
        Uri uri=Uri.parse(path);
        bod.setVideoURI(uri);
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        register();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sensitivity.setText("Sensitivity("+i+")");
                switch(i){
                    case 0:{
                        SHAKE_THRESHOLD=6.75f;
                        break;
                    }
                    case 1:{
                        SHAKE_THRESHOLD=6.0f;
                        break;
                    }
                    case 2:{
                        SHAKE_THRESHOLD=5.25f;
                        break;
                    }
                    case 3:{
                        SHAKE_THRESHOLD=4.5f;
                        break;
                    }
                    case 4:{
                        SHAKE_THRESHOLD=3.75f;
                        break;
                    }
                    case 5:{
                        SHAKE_THRESHOLD=3.0f;
                        break;
                    }
                    case 6:{
                        SHAKE_THRESHOLD=2.0f;
                        break;
                    }
                    case 7:{
                        SHAKE_THRESHOLD=2.25f;
                        break;
                    }
                    case 8:{
                        SHAKE_THRESHOLD=1.5f;
                        break;
                    }
                    case 9:{
                        SHAKE_THRESHOLD=0.75f;
                        break;
                    }
                    case 10:{
                        SHAKE_THRESHOLD=0.0f;
                        break;
                    }
                }
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
                if (!bod.isPlaying())
                    bod.start();
            }else {
                //pause video
                countpause++;
                if(countpause>2&&SHAKE_THRESHOLD!=0.0f) {
                    bod.pause();
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
        mSensorMgr.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
