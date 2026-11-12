package com.example.simcra2.Model;

/**
 * Ficha de una persona desaparecida en la historia (todos los datos son inventados).
 */
public class Desaparecido {
    public int id; // Identificador único
    public String nombre;
    public int edad;
    public String descripcion; // Descripción  situación
    public double latitud; // Ubicación latitud
    public double longitud; // Ubicación longitud
    public int diaDesaparicion; // Día en que desapareció

    // Constructor que inicializa todos los campos
    /**
     * @param id              identificador de la ficha
     * @param nombre          nombre de la persona
     * @param edad            edad
     * @param descripcion     dónde y cómo se la vio por última vez
     * @param latitud         latitud del último lugar conocido
     * @param longitud        longitud del último lugar conocido
     * @param diaDesaparicion día de la historia (1 a 7) en que aparece la ficha
     */
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