package com.example.accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    Sensor accelerometer;
    Button ready_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ready_button = findViewById(R.id.ready_button);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //permission to use the senson
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //getting sensor
        sensorManager.registerListener(WelcomeActivity.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        ready_button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        ready_button.setClickable(false);
        if(sensorEvent.values[0]>=-4 && sensorEvent.values[0]<=4 && sensorEvent.values[1]<=9.5 && sensorEvent.values[1]>=8.2 && sensorEvent.values[2]>=-4.5 && sensorEvent.values[2]<=4.5)
        {


            ready_button.setAlpha(1);
            ready_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ready_button.setClickable(true);
            ready_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(i);
                }
            });
        }

        else{

            ready_button.setAlpha(0.5f);
            ready_button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            ready_button.setClickable(false);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
