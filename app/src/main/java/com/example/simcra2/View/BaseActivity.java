package com.example.simcra2.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simcra2.Controller.AlertasManager;
import com.example.simcra2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;

public abstract class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
    }

    private void agregarBotonDesarrollador() {
        Button btnDev = new Button(this);
        btnDev.setText("⏩");
        btnDev.setTextSize(18f);
        btnDev.setBackgroundColor(0xFFCCCCCC);
        btnDev.setAlpha(0.85f);
        btnDev.setPadding(24, 12, 24, 12);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END | Gravity.BOTTOM;
        params.setMargins(0, 0, 32, 32);
        btnDev.setLayoutParams(params);

        btnDev.setOnClickListener(v -> {
            AlertasManager.avanzarAlertaManual(this);
            Toast.makeText(this, "⏩ Avanzaste a la siguiente alerta", Toast.LENGTH_SHORT).show();
            recreate(); // recarga la pantalla para mostrar nuevas alertas
        });

        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.addView(btnDev);

    }

    protected void configurarBottomNavigation(int selectedItemId) {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView == null) return;

        BottomNavigationView.OnItemSelectedListener listener = item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_estadisticas) {
                SharedPreferences prefs = getSharedPreferences("PreferenciasApp", MODE_PRIVATE);
                String hoy = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? LocalDate.now().toString() : "";
                String fechaEncuesta = prefs.getString("fecha_encuesta", "");

                if (!hoy.equals(fechaEncuesta)) {
                    if (!(BaseActivity.this instanceof EncuestaEmocional)) {
                        startActivity(new Intent(BaseActivity.this, EncuestaEmocional.class));
                        finish();
                    }
                } else {
                    if (!(BaseActivity.this instanceof EstadisticasRecursos)) {
                        startActivity(new Intent(BaseActivity.this, EstadisticasRecursos.class));
                        finish();
                    }
                }
                return true;
            }

            if (itemId == R.id.nav_desaparecidos) {
                if (!(BaseActivity.this instanceof Desaparecidos)) {
                    startActivity(new Intent(BaseActivity.this, Desaparecidos.class));
                    finish();
                }
                return true;
            }

            if (itemId == R.id.nav_home) {
                if (!(BaseActivity.this instanceof General)) {
                    startActivity(new Intent(BaseActivity.this, General.class));
                    finish();
                }
                return true;
            }

            if (itemId == R.id.nav_alert) {
                if (!(BaseActivity.this instanceof AlertasRegistradas)) {
                    startActivity(new Intent(BaseActivity.this, AlertasRegistradas.class));
                    finish();
                }
                return true;
            }


            return false;
        };

        bottomNavigationView.setOnItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(selectedItemId);
        bottomNavigationView.setOnItemSelectedListener(listener);
    }

}