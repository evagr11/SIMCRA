package com.example.simcra2.View;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.simcra2.Model.Alerta;
import com.example.simcra2.R;
import com.example.simcra2.Controller.AlertasManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AlertasRegistradas extends BaseActivity {

    private ListView listView;
    private AlertaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas_registradas);


        SharedPreferences prefs = getSharedPreferences("PreferenciasApp", MODE_PRIVATE);

        // Por si se llega aquí sin haber pasado por la pantalla de inicio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                !prefs.contains("fecha_inicio_alertas")) {
            prefs.edit().putString("fecha_inicio_alertas", LocalDate.now().toString()).apply();
            AlertasManager.generarAlertas(this);
        }

        listView = findViewById(R.id.listViewAlertas);

        List<Alerta> alertas = AlertasManager.obtenerAlertasDisponibles(this);
        Collections.reverse(alertas);

        if (alertas.isEmpty()) {
            Toast.makeText(this, "No hay alertas disponibles aún", Toast.LENGTH_SHORT).show();
        }

        adapter = new AlertaAdapter(this, alertas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Alerta seleccionada = alertas.get(position);
            AlertasManager.abrirDetalle(this, seleccionada);
        });

        configurarBottomNavigation(R.id.nav_alert);
    }
}