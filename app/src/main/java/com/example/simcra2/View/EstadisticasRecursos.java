package com.example.simcra2.View;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.simcra2.R;


public class EstadisticasRecursos extends BaseActivity {

    private ProgressBar barAgua, barEnergia, barComida, barMoral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_recursos);

        barAgua = findViewById(R.id.barAgua);
        barEnergia = findViewById(R.id.barEnergia);
        barComida = findViewById(R.id.barComida);
        barMoral = findViewById(R.id.barMoral);

        actualizarBarras();

        configurarBottomNavigation(R.id.nav_estadisticas);

    }


    private void actualizarBarras() {
        java.util.Random random = new java.util.Random();

        barAgua.setProgress(random.nextInt(71) + 30);
        barEnergia.setProgress(random.nextInt(71) + 30);
        barComida.setProgress(random.nextInt(71) + 30);
        barMoral.setProgress(random.nextInt(71) + 30);
    }

}