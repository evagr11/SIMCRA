package com.example.simcra2.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.simcra2.R;

import java.time.LocalDate;


public class EncuestaEmocional extends BaseActivity {

    private static final String PREFS = "PreferenciasApp";
    private static final String CLAVE_ENCUESTA = "encuesta_realizada";
    private static final String CLAVE_FECHA = "fecha_encuesta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta_emocional);

        Button btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            String hoy = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hoy = LocalDate.now().toString();
            }

            prefs.edit()
                    .putBoolean(CLAVE_ENCUESTA, true)
                    .putString(CLAVE_FECHA, hoy)
                    .apply();

            startActivity(new Intent(this, EstadisticasRecursos.class));
            finish();
        });


        configurarBottomNavigation(R.id.nav_estadisticas);


    }
}