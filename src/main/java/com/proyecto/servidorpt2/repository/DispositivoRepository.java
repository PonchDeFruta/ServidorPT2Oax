package com.proyecto.servidorpt2.repository;

import com.proyecto.servidorpt2.entities.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Integer> {

    Optional<Dispositivo> findByNombreDispositivoAndAnuncio_IdMensaje(String nombreDispositivo, Integer idMensaje);
}
