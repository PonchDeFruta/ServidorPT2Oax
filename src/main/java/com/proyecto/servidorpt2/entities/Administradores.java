package com.proyecto.servidorpt2.entities;

import jakarta.persistence.*;
@Entity
@Table(name="administradores")

public class Administradores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_administrador")
    private Integer idAdministrador;

    @Column(name = "usuario", length = 255, nullable = false)
    private String usuario;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 255, nullable = false)
    private String apellido;

    // Getters y Setters
    public Integer getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(Integer idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
