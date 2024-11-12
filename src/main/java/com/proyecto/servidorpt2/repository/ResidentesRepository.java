package com.proyecto.servidorpt2.repository;
import com.proyecto.servidorpt2.entities.Residentes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentesRepository extends JpaRepository<Residentes,Integer>{
    // Consulta personalizada para obtener solo los residentes con domicilio
    @Query("SELECT r FROM Residentes r LEFT JOIN FETCH r.domicilio WHERE r.domicilio IS NOT NULL")
    List<Residentes> findResidentesWithDomicilio();

}

