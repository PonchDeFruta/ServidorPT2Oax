package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.entities.Administradores;
import com.proyecto.servidorpt2.repository.AdministradoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradoresService {

    @Autowired
    private AdministradoresRepository administradoresRepository;

    // Obtener todos los administradores
    public List<Administradores> obtenerTodosLosAdministradores() {
        return administradoresRepository.findAll();
    }

    // Obtener un administrador por su ID
    public Optional<Administradores> obtenerAdministradorPorId(Integer id) {
        return administradoresRepository.findById(id);
    }

    // Crear o actualizar un administrador
    public Administradores guardarAdministrador(Administradores administrador) {
        return administradoresRepository.save(administrador);
    }

    // Eliminar un administrador por su ID
    public void eliminarAdministrador(Integer id) {
        administradoresRepository.deleteById(id);
    }
}
