package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.utils.Encriptar;
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
    @Autowired
    private Encriptar encryptionService;

    // Obtener todos los administradores
    public List<Administradores> obtenerTodosLosAdministradores() {
        List<Administradores> administradores = administradoresRepository.findAll();
        administradores.forEach(this::desencriptarAdministrador);
        return administradores;
    }

    // Obtener un administrador por su ID
    public Optional<Administradores> obtenerAdministradorPorId(Integer id) {
        Optional<Administradores> administrador = administradoresRepository.findById(id);
        administrador.ifPresent(this::desencriptarAdministrador);
        return administrador;
    }

    // Guardar o actualizar un administrador (crea si no existe o actualiza si ya existe)
    public Administradores guardarAdministrador(Administradores administrador) {
        encriptarAdministrador(administrador);  // Cifrar datos sensibles antes de guardar
        return administradoresRepository.save(administrador);
    }

    // Eliminar un administrador por su ID
    public void eliminarAdministrador(Integer id) {
        administradoresRepository.deleteById(id);
    }

    // Métodos auxiliares para cifrar y descifrar
    private void encriptarAdministrador(Administradores administrador) {
        try {
            administrador.setNombre(encryptionService.encrypt(administrador.getNombre()));
            administrador.setApellido(encryptionService.encrypt(administrador.getApellido()));
            // Agrega más campos a cifrar según sea necesario
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar datos del administrador", e);
        }
    }

    private void desencriptarAdministrador(Administradores administrador) {
        try {
            administrador.setNombre(encryptionService.decrypt(administrador.getNombre()));
            administrador.setApellido(encryptionService.decrypt(administrador.getApellido()));
            // Agrega más campos a descifrar según sea necesario
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar datos del administrador", e);
        }
    }


}
