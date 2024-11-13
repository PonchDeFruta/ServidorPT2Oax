package com.proyecto.servidorpt2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "anuncio")
@Getter
@Setter
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "contenido_del_mensaje", length = 255, nullable = false)
    private String contenidoDelMensaje;

    @Column(name = "fecha_mensaje", nullable = false)
    private LocalDateTime fechaMensaje;

    @Column(name = "es_audio", nullable = false)
    private boolean esAudio;

    // Relación Many-to-One con Residentes, ahora permitiendo valores nulos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_residente", referencedColumnName = "id_residente", nullable = true)
    private Residentes residente;

}
