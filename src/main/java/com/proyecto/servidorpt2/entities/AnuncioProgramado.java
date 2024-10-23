package com.proyecto.servidorpt2.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anuncio_programado")
public class AnuncioProgramado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje_programado")
    private Integer idMensajeProgramado;

    @Column(name = "fecha_mensaje_programado", nullable = false)
    private LocalDateTime fechaMensajeProgramado;

    @Column(name = "estatus_msj", length = 255, nullable = false)
    private String estatusMsj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mensaje", nullable = false)  // La columna que mapea la relación a Anuncio
    private Anuncio anuncio;

    // Ge
    // Getters y Setters
    public Integer getIdMensajeProgramado() {
        return idMensajeProgramado;
    }

    public void setIdMensajeProgramado(Integer idMensajeProgramado) {
        this.idMensajeProgramado = idMensajeProgramado;
    }

    public LocalDateTime getFechaMensajeProgramado() {
        return fechaMensajeProgramado;
    }

    public void setFechaMensajeProgramado(LocalDateTime fechaMensajeProgramado) {
        this.fechaMensajeProgramado = fechaMensajeProgramado;
    }

    public String getEstatusMsj() {
        return estatusMsj;
    }

    public void setEstatusMsj(String estatusMsj) {
        this.estatusMsj = estatusMsj;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }
}
