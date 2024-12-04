package com.proyecto.servidorpt2.repository;

import com.proyecto.servidorpt2.entities.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {
    // Consulta para obtener los anuncios por id del residente
    @Query("SELECT a FROM Anuncio a WHERE a.residente.idResidente = :idResidente")
    List<Anuncio> findByResidenteIdResidente(@Param("idResidente") Integer idResidente);
}
