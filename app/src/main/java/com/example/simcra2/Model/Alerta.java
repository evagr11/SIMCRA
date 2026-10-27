package com.example.simcra2.Model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class Alerta implements Serializable {
    private String fechaHoraOriginal;
    private String titular;
    private String resumen;
    private String contenidoExtendido;

    public Alerta(String fechaHoraOriginal, String titular, String resumen, String contenidoExtendido) {
        this.fechaHoraOriginal = fechaHoraOriginal;
        this.titular = titular;
        this.resumen = resumen;
        this.contenidoExtendido = contenidoExtendido;
    }

    public String getFechaHoraOriginal() { return fechaHoraOriginal; }
    public String getTitular() { return titular; }
    public String getResumen() { return resumen; }
    public String getContenidoExtendido() { return contenidoExtendido; }

    // ✅ Nuevo metodo para obtener la fecha en milisegundos
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