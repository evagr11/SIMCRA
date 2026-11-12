package com.example.simcra2.Model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

/**
 * Una alerta de la historia: fecha y hora, titular, resumen y texto ampliado.
 *
 * <p>Es {@code Serializable} para poder pasarla en un Intent a {@code DetalleAlerta}.</p>
 */
public class Alerta implements Serializable {
    private String fechaHoraOriginal;
    private String titular;
    private String resumen;
    private String contenidoExtendido;

    /**
     * @param fechaHoraOriginal  fecha y hora de llegada, con el formato {@code yyyy-MM-dd HH:mm}
     * @param titular            titular de la alerta
     * @param resumen            texto corto que sale en la lista y en la notificación
     * @param contenidoExtendido texto completo que se ve al abrirla
     */
    public Alerta(String fechaHoraOriginal, String titular, String resumen, String contenidoExtendido) {
        this.fechaHoraOriginal = fechaHoraOriginal;
        this.titular = titular;
        this.resumen = resumen;
        this.contenidoExtendido = contenidoExtendido;
    }

    /**
     * @return la fecha y hora de llegada como texto
     */
    public String getFechaHoraOriginal() { return fechaHoraOriginal; }
    /**
     * @return el titular
     */
    public String getTitular() { return titular; }
    /**
     * @return el resumen
     */
    public String getResumen() { return resumen; }
    /**
     * @return el texto completo
     */
    public String getContenidoExtendido() { return contenidoExtendido; }

    // ✅ Nuevo metodo para obtener la fecha en milisegundos
    /**
     * Convierte la fecha de la alerta a milisegundos.
     *
     * @return la fecha en milisegundos, o la hora actual si no se puede leer
     */
    public long getFechaProgramadaEnMillis() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date fecha = formatter.parse(fechaHoraOriginal);
            return fecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis(); // Fallback en caso de error
        }
    }
}