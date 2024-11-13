package com.proyecto.servidorpt2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "domicilios")
@Getter
@Setter

public class Domicilios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_domicilio")
    private Integer idDomicilio;

    @Column(name = "direccion", length = 255, nullable = false)
    private String direccion;

    @Column(name = "referencia", length = 255)
    private String referencia;

    @Column(name = "coordenadas", length = 255)
    private String coordenadas;

    // Relación One-to-Many con Residentes
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Ignorar la serialización recursiva
    private List<Residentes> residentes;
}
