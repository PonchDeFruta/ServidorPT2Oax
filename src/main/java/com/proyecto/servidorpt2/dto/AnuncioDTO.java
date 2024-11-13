package com.proyecto.servidorpt2.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AnuncioDTO {
    private Integer idMensaje;
    private String titulo;
    private String contenidoDelMensaje;
    private String fechaMensaje; // Cambiado a String
    private boolean esAudio;
    private Integer idResidente;

    private ResidenteDTO residente;


}
