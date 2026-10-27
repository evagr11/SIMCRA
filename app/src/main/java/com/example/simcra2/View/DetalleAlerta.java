package com.example.simcra2.View;

import android.os.Bundle;
import android.widget.TextView;

import com.example.simcra2.Model.Alerta;
import com.example.simcra2.R;

public class DetalleAlerta extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alerta);

        TextView tvTitular = findViewById(R.id.tvTitular);
        TextView tvContenido = findViewById(R.id.tvContenido);

        Alerta alerta = (Alerta) getIntent().getSerializableExtra("alerta");

        tvTitular.setText(alerta.getTitular());
        tvContenido.setText(alerta.getContenidoExtendido());


        configurarBottomNavigation(R.id.nav_alert);

    }
}