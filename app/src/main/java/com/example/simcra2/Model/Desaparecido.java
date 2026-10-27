package com.example.simcra2.Model;

public class Desaparecido {
    public int id; // Identificador único
    public String nombre;
    public int edad;
    public String descripcion; // Descripción  situación
    public double latitud; // Ubicación latitud
    public double longitud; // Ubicación longitud
    public int diaDesaparicion; // Día en que desapareció

    // Constructor que inicializa todos los campos
    public Desaparecido(int id, String nombre, int edad, String descripcion, double latitud, double longitud, int diaDesaparicion) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.diaDesaparicion = diaDesaparicion;
    }
}