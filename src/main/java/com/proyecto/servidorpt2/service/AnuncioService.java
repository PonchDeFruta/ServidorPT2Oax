package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    // Obtener todos los anuncios
    public List<Anuncio> obtenerTodosLosAnuncios() {
        return anuncioRepository.findAll();
    }

    // Obtener un anuncio por su ID
    public Optional<Anuncio> obtenerAnuncioPorId(Integer id) {
        return anuncioRepository.findById(id);
    }

    // Crear o actualizar un anuncio
    public Anuncio guardarAnuncio(Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }

    // Eliminar un anuncio por su ID
    public void eliminarAnuncio(Integer id) {
        anuncioRepository.deleteById(id);
    }
}
