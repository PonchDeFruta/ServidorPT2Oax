package com.proyecto.servidorpt2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "residentes")
@Getter
@Setter
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

    // Relación Many-to-One con Domicilios
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_domicilio", referencedColumnName = "id_domicilio", nullable = true)
    private Domicilios domicilio;
}
