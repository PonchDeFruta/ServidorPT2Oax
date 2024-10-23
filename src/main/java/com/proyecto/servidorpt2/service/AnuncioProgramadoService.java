package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.entities.AnuncioProgramado;
import com.proyecto.servidorpt2.repository.AnuncioProgramadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnuncioProgramadoService {

    @Autowired
    private AnuncioProgramadoRepository anuncioProgramadoRepository;

    // Obtener todos los anuncios programados
    public List<AnuncioProgramado> obtenerTodosLosAnunciosProgramados() {
        return anuncioProgramadoRepository.findAll();
    }

    // Obtener un anuncio programado por su ID
    public Optional<AnuncioProgramado> obtenerAnuncioProgramadoPorId(Integer id) {
        return anuncioProgramadoRepository.findById(id);
    }

    // Crear o actualizar un anuncio programado
    public AnuncioProgramado guardarAnuncioProgramado(AnuncioProgramado anuncioProgramado) {
        return anuncioProgramadoRepository.save(anuncioProgramado);
    }

    // Eliminar un anuncio programado por su ID
    public void eliminarAnuncioProgramado(Integer id) {
        anuncioProgramadoRepository.deleteById(id);
    }
}
