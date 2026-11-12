package com.example.simcra2.Controller;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.example.simcra2.View.General;
import com.example.simcra2.View.Info1;

/**
 * Lógica de la pantalla de inicio: primera vez, canal de notificaciones,
 * permisos y arranque de la historia.
 */
public class LandingController {

    /**
     * Nombre del fichero de preferencias de la app.
     */
    public static final String PREFS_APP = "PreferenciasApp";
    /**
     * Clave que indica si es la primera vez que se abre la app.
     */
    public static final String KEY_PRIMERA_VEZ = "esPrimeraVez";

    private boolean yaVerificado = false;

    /**
     * La primera vez lleva a la introducción y las siguientes a la pantalla de inicio.
     *
     * @param context contexto de la app
     * @return el Intent de la pantalla a la que hay que ir
     */
    public Intent obtenerIntentAcceso(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_APP, Context.MODE_PRIVATE);
        boolean esPrimeraVez = prefs.getBoolean(KEY_PRIMERA_VEZ, true);

        if (esPrimeraVez) {
            prefs.edit().putBoolean(KEY_PRIMERA_VEZ, false).apply();
            return new Intent(context, Info1.class);
        } else {
            return new Intent(context, General.class);
        }
    }

    /**
     * Crea el canal {@code canal_alertas} con importancia alta (Android 8 o superior).
     *
     * @param context contexto de la app
     */
    public void crearCanalNotificacion(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    "canal_alertas",
                    "Alertas Simcra",
                    NotificationManager.IMPORTANCE_HIGH
            );
            canal.setDescription("Notificaciones importantes de alertas");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }
    }

    /**
     * @param context contexto de la app
     * @return {@code true} si es Android 13 o superior y aún no hay permiso para notificar
     */
    public boolean necesitaPermisoNotificaciones(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Pide el permiso {@code POST_NOTIFICATIONS}.
     *
     * @param activity Activity que recibe la respuesta
     */
    public void solicitarPermisoNotificaciones(android.app.Activity activity) {
        activity.requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
    }

    /**
     * Comprueba el permiso de alarmas exactas (Android 12 o superior); si falta,
     * abre los ajustes, y si está, arranca la historia.
     *
     * @param context contexto de la app
     */
    public void verificarAlarmasExactas(Context context) {
        if (yaVerificado) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "Activa las alarmas exactas para recibir alertas programadas", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
                return;
            }
        }

        yaVerificado = true;
        iniciarSimcra(context);
    }

    // La historia de 7 días empieza el día que se abre la app por primera vez.
    // Si ya se generó, no se vuelve a crear para que las alertas sigan avanzando día a día.
    /**
     * Guarda la fecha de inicio y genera y programa las alertas, solo la primera vez,
     * para que la historia siga avanzando día a día.
     *
     * @param context contexto de la app
     */
    public void iniciarSimcra(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_APP, Context.MODE_PRIVATE);
        if (prefs.contains("fecha_inicio_alertas")) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prefs.edit().putString("fecha_inicio_alertas", java.time.LocalDate.now().toString()).apply();
        }
        AlertasManager.generarAlertas(context);
        AlertasManager.programarTodasLasAlertas(context);
    }

    /**
     * @return {@code true} si ya se han comprobado las alarmas exactas
     */
    public boolean yaVerificado() {
        return yaVerificado;
    }

    /**
     * @param valor si ya se han comprobado las alarmas exactas
     */
    public void setYaVerificado(boolean valor) {
        yaVerificado = valor;
    }
}
