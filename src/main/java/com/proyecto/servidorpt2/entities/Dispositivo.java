package com.proyecto.servidorpt2.entities;
import jakarta.persistence.*;
@Entity
@Table(name="dispositivo")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo")
    private Integer idDispositivo;

    @Column(name = "contador_recepcion_mensajes", nullable = false)
    private Integer contadorRecepcionMensajes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mensaje", referencedColumnName = "id_mensaje", nullable = false)
    private Anuncio anuncio;  // Relación Many-to-One con Anuncio (o Mensaje)

    // Getters y Setters
    public Integer getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(Integer idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public Integer getContadorRecepcionMensajes() {
        return contadorRecepcionMensajes;
    }

    public void setContadorRecepcionMensajes(Integer contadorRecepcionMensajes) {
        this.contadorRecepcionMensajes = contadorRecepcionMensajes;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

}
