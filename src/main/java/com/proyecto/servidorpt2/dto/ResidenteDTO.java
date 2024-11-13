package com.proyecto.servidorpt2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResidenteDTO {
    private Integer idResidente;
    private String nombre;
    private String apellido;
    private String apodo;
    private String comercio;

    private DomicilioDTO domicilio;

}
