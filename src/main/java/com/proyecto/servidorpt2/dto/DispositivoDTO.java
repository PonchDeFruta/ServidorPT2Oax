package com.proyecto.servidorpt2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DispositivoDTO {
    private Integer idDispositivo;
    private String nombreDispositivo;
    private Integer contadorRecepcionMensajes;
    private Integer idMensaje; // Solo id del anuncio, no todo el objeto

    // Puedes agregar más campos si necesitas más información del anuncio
}
