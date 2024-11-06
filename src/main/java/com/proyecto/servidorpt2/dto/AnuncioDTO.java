package com.proyecto.servidorpt2.dto;

public class AnuncioDTO {
    private Integer idMensaje;
    private String titulo;
    private String contenidoDelMensaje;
    private String fechaMensaje; // Cambiado a String
    private boolean esAudio;
    private Integer idResidente;

    // Getters y Setters
    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenidoDelMensaje() {
        return contenidoDelMensaje;
    }

    public void setContenidoDelMensaje(String contenidoDelMensaje) {
        this.contenidoDelMensaje = contenidoDelMensaje;
    }

    public String getFechaMensaje() {
        return fechaMensaje;
    }

    public void setFechaMensaje(String fechaMensaje) {
        this.fechaMensaje = fechaMensaje;
    }

    public boolean isEsAudio() {
        return esAudio;
    }

    public void setEsAudio(boolean esAudio) {
        this.esAudio = esAudio;
    }

    public Integer getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(Integer idResidente) {
        this.idResidente = idResidente;
    }
}
