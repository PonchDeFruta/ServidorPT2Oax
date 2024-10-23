package com.proyecto.servidorpt2.dto;

import java.time.LocalDateTime;

public class AnuncioProgramadoDTO {

    private String contenidoDelMensaje;
    private LocalDateTime fechaMensaje;

    // Fecha programada para publicar el anuncio (opcional)
    private LocalDateTime fechaProgramada;

    // Getters y Setters
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

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }
}
