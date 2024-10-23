package com.proyecto.servidorpt2.repository;

import com.proyecto.servidorpt2.entities.Administradores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradoresRepository extends JpaRepository<Administradores, Integer> {
}
