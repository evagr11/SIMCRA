package com.example.simcra2.Controller;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.simcra2.R;

/**
 * Lanza una notificación inmediata en el canal de alertas.
 */
public class NotificacionSimcra {

    /**
     * Muestra la notificación si hay permiso para notificar.
     *
     * @param context contexto de la app
     * @param titulo  título de la notificación
     * @param mensaje texto de la notificación
     */
    public static void lanzar(Context context, String titulo, String mensaje) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // El permiso no está concedido, no lanzar la notificación
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "canal_alertas")
                .setSmallIcon(R.mipmap.ic_alert) // Usa tu icono aquí
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

}