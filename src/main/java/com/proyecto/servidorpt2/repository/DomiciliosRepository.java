package com.proyecto.servidorpt2.repository;

import com.proyecto.servidorpt2.entities.Domicilios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomiciliosRepository extends JpaRepository<Domicilios, Integer> {
}
