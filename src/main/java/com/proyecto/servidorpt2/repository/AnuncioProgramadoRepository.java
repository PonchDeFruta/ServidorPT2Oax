package com.proyecto.servidorpt2.repository;

import com.proyecto.servidorpt2.entities.AnuncioProgramado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;
@Repository
public interface AnuncioProgramadoRepository extends JpaRepository<AnuncioProgramado, Integer> {
    List<AnuncioProgramado> findByFechaMensajeProgramadoBeforeAndEstatusMsj(LocalDateTime fecha, String estatus);

}
