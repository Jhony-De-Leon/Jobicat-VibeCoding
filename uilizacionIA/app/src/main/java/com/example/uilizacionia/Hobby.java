package com.example.uilizacionia;

public class Hobby {
    private int id;
    private String nombre;
    private String dificultad;

    // Constructor vacío
    public Hobby() {
    }

    // Constructor con parámetros
    public Hobby(String nombre, String dificultad) {
        this.nombre = nombre;
        this.dificultad = dificultad;
    }

    // Constructor completo
    public Hobby(int id, String nombre, String dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.dificultad = dificultad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    @Override
    public String toString() {
        return nombre + " (" + dificultad + ")";
    }
}
