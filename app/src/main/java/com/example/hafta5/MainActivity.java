package com.example.hafta5;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private TextView tvSensorValues;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tvSensorValues = findViewById(R.id.tvSensorValues);

        Button btnAccelerometer = findViewById(R.id.btnAccelerometer);
        btnAccelerometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSensorListener(Sensor.TYPE_ACCELEROMETER);
            }
        });

        Button btnGyroscope = findViewById(R.id.btnGyroscope);
        btnGyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSensorListener(Sensor.TYPE_GYROSCOPE);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void registerSensorListener(int sensorType) {
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            tvSensorValues.setText("Bu cihazda bu sensör bulunamadı.");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder sensorValues = new StringBuilder();
        for (int i = 0; i < event.values.length; i++) {
            sensorValues.append("Value ").append(i).append(": ").append(event.values[i]).append("\n");
        }
        tvSensorValues.setText(sensorValues.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onLocationChanged(Location location) {
        tvSensorValues.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
    }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Lütfen GPS'i etkinleştirin", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
    }
}