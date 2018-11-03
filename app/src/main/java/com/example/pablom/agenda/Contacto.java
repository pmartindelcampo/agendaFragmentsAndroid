package com.example.pablom.agenda;

public class Contacto {
    private int id;
    private String nombre;
    private String direccion;
    private String movil;
    private String email;

    public Contacto(int id, String nombre, String direccion, String movil, String email) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.movil = movil;
        this.email = email;
    }

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
