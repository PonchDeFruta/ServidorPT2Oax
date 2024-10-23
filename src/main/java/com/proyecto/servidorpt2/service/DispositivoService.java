package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.entities.Dispositivo;
import com.proyecto.servidorpt2.repository.DispositivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    // Obtener todos los dispositivos
    public List<Dispositivo> obtenerTodosLosDispositivos() {
        return dispositivoRepository.findAll();
    }

    // Obtener un dispositivo por su ID
    public Optional<Dispositivo> obtenerDispositivoPorId(Integer id) {
        return dispositivoRepository.findById(id);
    }

    // Crear o actualizar un dispositivo
    public Dispositivo guardarDispositivo(Dispositivo dispositivo) {
        return dispositivoRepository.save(dispositivo);
    }

    // Eliminar un dispositivo por su ID
    public void eliminarDispositivo(Integer id) {
        dispositivoRepository.deleteById(id);
    }
}
