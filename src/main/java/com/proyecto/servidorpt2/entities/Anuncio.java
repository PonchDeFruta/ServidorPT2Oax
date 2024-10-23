package com.proyecto.servidorpt2.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "anuncio")
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    @Column(name = "contenido_del_mensaje", length = 255, nullable = false)
    private String contenidoDelMensaje;

    @Column(name = "fecha_mensaje", nullable = false)
    private LocalDateTime fechaMensaje;

    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnuncioProgramado> anunciosProgramados;

    // Getters y Setters
    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getContenidoDelMensaje() {
        return contenidoDelMensaje;
    }

    public void setContenidoDelMensaje(String contenidoDelMensaje) {
        this.contenidoDelMensaje = contenidoDelMensaje;
    }

    public LocalDateTime getFechaMensaje() {
        return fechaMensaje;
    }

    public void setFechaMensaje(LocalDateTime fechaMensaje) {
        this.fechaMensaje = fechaMensaje;
    }

    public List<AnuncioProgramado> getAnunciosProgramados() {
        return anunciosProgramados;
    }

    public void setAnunciosProgramados(List<AnuncioProgramado> anunciosProgramados) {
        this.anunciosProgramados = anunciosProgramados;
    }
}
