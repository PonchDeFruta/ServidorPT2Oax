package com.proyecto.servidorpt2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="administradores")
@Getter
@Setter

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
}
