package com.example.simcra2.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.simcra2.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class General extends BaseActivity {

    private SensorManager sensorManager;
    private SensorEventListener sensorListener;
    private BroadcastReceiver batteryReceiver;
    private TextView txtFecha, txtBateria, txtHardware, txtGiroscopio, txtAcelerometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_general);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtFecha = findViewById(R.id.txtFecha);
        txtBateria = findViewById(R.id.txtBateria);
        txtHardware = findViewById(R.id.txtHardware);
        txtGiroscopio = findViewById(R.id.txtGiroscopio);
        txtAcelerometro = findViewById(R.id.txtAcelerometro);

        txtFecha.setText(obtenerFecha());
        configurarBottomNavigation(R.id.nav_home);

        // Batería en tiempo real
        Bateria();

        // Info de hardware
        Hardware();

        // Sensores en tiempo real
        Sensores();

    }

    public void Bateria(){
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int batteryPct = (int) ((level / (float) scale) * 100);
                txtBateria.setText(batteryPct + "%");
            }
        };
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void Hardware(){
        String modelo = Build.MODEL;
        String fabricante = Build.MANUFACTURER;
        String versionAndroid = Build.VERSION.RELEASE;
        String hardware = "Dispositivo: " + fabricante + " \n " + modelo + "\nAndroid: " + versionAndroid;
        txtHardware.setText(hardware);
    }

    public void Sensores(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorGiroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    txtGiroscopio.setText(String.format("X: %.2f\nY: %.2f\nZ: %.2f", event.values[0], event.values[1], event.values[2]));
                } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    txtAcelerometro.setText(String.format("X: %.2f\nY: %.2f\nZ: %.2f", event.values[0], event.values[1], event.values[2]));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        sensorManager.registerListener(sensorListener, sensorGiroscopio, SensorManager.SENSOR_DELAY_UI);

        sensorManager.registerListener(sensorListener, sensorAcelerometro, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
        }
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && sensorListener != null) {
            Sensor sensorGiroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            Sensor sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensorGiroscopio != null) {
                sensorManager.registerListener(sensorListener, sensorGiroscopio, SensorManager.SENSOR_DELAY_UI);
            }
            if (sensorAcelerometro != null) {
                sensorManager.registerListener(sensorListener, sensorAcelerometro, SensorManager.SENSOR_DELAY_UI);
            }
        }
        if (batteryReceiver != null) {
            registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
    }

    private static String obtenerFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("EEEE d MMMM yyyy", new Locale("es", "ES"));
        String fecha = formato.format(new Date());
        return "Hoy es " + fecha.substring(0, 1).toUpperCase() + fecha.substring(1);
    }
}