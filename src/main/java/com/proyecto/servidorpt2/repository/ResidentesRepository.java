package com.proyecto.servidorpt2.repository;
import com.proyecto.servidorpt2.entities.Residentes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentesRepository extends JpaRepository<Residentes,Integer>{

}

