package com.example.simcra2.View;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simcra2.Controller.LandingController;
import com.example.simcra2.R;

/**
 * Pantalla de carga: pide los permisos y arranca la historia con {@link com.example.simcra2.Controller.LandingController}.
 */
public class LandingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnAcceder;
    private LandingController controller;

    /**
     * Enseña la barra de carga, crea el canal y pide los permisos que falten.
     *
     * @param savedInstanceState estado guardado de la Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        controller = new LandingController();

        btnAcceder = findViewById(R.id.btnAccederInfo);
        progressBar = findViewById(R.id.progressBar);

        btnAcceder.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            btnAcceder.setVisibility(View.VISIBLE);
        }, 1000);

        btnAcceder.setOnClickListener(v -> {
            btnAcceder.setEnabled(false);
            btnAcceder.setText("Accediendo...");
            Intent intent = controller.obtenerIntentAcceso(this);
            startActivity(intent);
            finish();
        });

        controller.crearCanalNotificacion(this);

        if (controller.necesitaPermisoNotificaciones(this)) {
            controller.solicitarPermisoNotificaciones(this);
        } else {
            controller.verificarAlarmasExactas(this);
        }
    }

    /**
     * Si se concede el permiso de notificaciones, comprueba las alarmas exactas.
     *
     * @param requestCode  código de la petición (100)
     * @param permissions  permisos pedidos
     * @param grantResults resultado de cada permiso
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                controller.verificarAlarmasExactas(this);
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Al volver de los ajustes, vuelve a comprobar las alarmas exactas si aún no se ha hecho.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (!controller.yaVerificado()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                controller.verificarAlarmasExactas(this);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                controller.verificarAlarmasExactas(this);
            }
        }
    }
}
