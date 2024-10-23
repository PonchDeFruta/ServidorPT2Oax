package com.proyecto.servidorpt2.entities;

import jakarta.persistence.*;
@Entity
@Table(name="residentes")
public class Residentes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_residente")
    private Integer idResidente;

    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 255, nullable = false)
    private String apellido;

    @Column(name = "apodo", length = 255)
    private String apodo;

    @Column(name = "comercio", length = 255)
    private String comercio;

    // Relación Many-to-One con Domicilio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_domicilio", referencedColumnName = "id_domicilio", nullable = false)
    private Domicilios domicilio;

    // Getters y Setters
    public Integer getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(Integer idResidente) {
        this.idResidente = idResidente;
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

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getComercio() {
        return comercio;
    }

    public void setComercio(String comercio) {
        this.comercio = comercio;
    }

    public Domicilios getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilios domicilio) {
        this.domicilio = domicilio;
    }
}
