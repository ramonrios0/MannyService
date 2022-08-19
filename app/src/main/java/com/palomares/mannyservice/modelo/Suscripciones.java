package com.palomares.mannyservice.modelo;

public class Suscripciones {
    private String fecha,nombre,precio,ciclo,color,icono, tiempo, idNotificacion;

    public Suscripciones() {
    }

    public Suscripciones(String fecha, String nombre, String precio, String ciclo, String color, String icono, String tiempo, String idNotificacion) {
        this.fecha = fecha;
        this.nombre = nombre;
        this.precio = precio;
        this.ciclo = ciclo;
        this.color = color;
        this.icono = icono;
        this.tiempo = tiempo;
        this.idNotificacion = idNotificacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        this.idNotificacion = idNotificacion;
    }
}

