package com.proyecto.servidorpt2.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Entity
@Table(name="dispositivo")
@Getter
@Setter

public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo")
    private Integer idDispositivo;

    @Column(name = "nombre_dispositivo", length = 100, nullable = false)
    private String nombreDispositivo;  // Nuevo campo para el nombre del dispositivo

    @Column(name = "contador_recepcion_mensajes", nullable = false)
    private Integer contadorRecepcionMensajes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mensaje", referencedColumnName = "id_mensaje", nullable = false)
    private Anuncio anuncio;  // Relación Many-to-One con Anuncio (o Mensaje)


}
