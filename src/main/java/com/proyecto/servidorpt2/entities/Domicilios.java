package com.proyecto.servidorpt2.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "domicilios")
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

    // Relación Many-to-One con Residentes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_residente", referencedColumnName = "id_residente", nullable = false)
    private Residentes residentes;  // Relación Many-to-One con la entidad Residentes

    // Getters y Setters
    public Integer getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(Integer idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Residentes getResidentes() {
        return residentes;
    }

    public void setResidentes(Residentes residentes) {
        this.residentes = residentes;
    }
}
