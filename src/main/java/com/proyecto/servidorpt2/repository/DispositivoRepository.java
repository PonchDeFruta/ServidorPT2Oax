package com.proyecto.servidorpt2.repository;

import com.proyecto.servidorpt2.entities.Dispositivo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Integer> {

    Optional<Dispositivo> findByNombreDispositivoAndAnuncio_IdMensaje(String nombreDispositivo, Integer idMensaje);

    public List<Dispositivo> findByAnuncioIdMensaje(Integer idMensaje);



}
