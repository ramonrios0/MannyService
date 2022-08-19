package com.palomares.mannyservice.modelo;

public class Servicios {
    private String nombre, icono, color;

    public Servicios(){}

    public Servicios(String nombre, String icono, String color) {
        this.nombre = nombre;
        this.icono = icono;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
