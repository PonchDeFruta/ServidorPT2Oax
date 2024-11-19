package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.Utils.Encriptar;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.repository.ResidentesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentesService {

    @Autowired
    private ResidentesRepository residentesRepository;

    @Autowired
    private Encriptar encryptionService;

    public List<Residentes> obtenerTodosLosResidentes() {
        List<Residentes> residentes = residentesRepository.findAll();
        residentes.forEach(this::desencriptarResidente);
        return residentes;
    }

    public Optional<Residentes> obtenerResidentePorId(Integer id) {
        Optional<Residentes> residente = residentesRepository.findById(id);
        residente.ifPresent(this::desencriptarResidente);
        return residente;
    }

    public Residentes guardarResidente(Residentes residente) {
        encriptarResidente(residente);
        return residentesRepository.save(residente);
    }

    public void eliminarResidente(Integer id) {
        residentesRepository.deleteById(id);
    }

    public List<Residentes> obtenerResidentesConDomicilio() {
        List<Residentes> residentes = residentesRepository.findResidentesWithDomicilio();
        residentes.forEach(this::desencriptarResidente);
        return residentes;
    }

    private void encriptarResidente(Residentes residente) {
        try {
            residente.setNombre(encryptionService.encrypt(residente.getNombre()));
            residente.setApellido(encryptionService.encrypt(residente.getApellido()));
            // Agrega más campos a cifrar según sea necesario
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar datos del residente", e);
        }
    }

    private void desencriptarResidente(Residentes residente) {
        try {
            residente.setNombre(encryptionService.decrypt(residente.getNombre()));
            residente.setApellido(encryptionService.decrypt(residente.getApellido()));
            // Agrega más campos a descifrar según sea necesario
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar datos del residente", e);
        }
    }
}
