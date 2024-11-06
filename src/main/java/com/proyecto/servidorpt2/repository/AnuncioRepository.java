package com.proyecto.servidorpt2.repository;

import com.proyecto.servidorpt2.entities.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {

}
