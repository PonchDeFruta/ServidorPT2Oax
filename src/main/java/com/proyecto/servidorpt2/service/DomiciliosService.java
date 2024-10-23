package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.repository.DomiciliosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DomiciliosService {

    @Autowired
    private DomiciliosRepository domiciliosRepository;

    // Obtener todos los domicilios
    public List<Domicilios> obtenerTodosLosDomicilios() {
        return domiciliosRepository.findAll();
    }

    // Obtener un domicilio por su ID
    public Optional<Domicilios> obtenerDomicilioPorId(Integer id) {
        return domiciliosRepository.findById(id);
    }

    // Crear o actualizar un domicilio
    public Domicilios guardarDomicilio(Domicilios domicilio) {
        return domiciliosRepository.save(domicilio);
    }

    // Eliminar un domicilio por su ID
    public void eliminarDomicilio(Integer id) {
        domiciliosRepository.deleteById(id);
    }
}
