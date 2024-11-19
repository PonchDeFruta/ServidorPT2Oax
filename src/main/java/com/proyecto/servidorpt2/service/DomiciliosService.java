package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.Utils.Encriptar;
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

    @Autowired
    private Encriptar encryptionService;

    public List<Domicilios> obtenerTodosLosDomicilios() {
        List<Domicilios> domicilios = domiciliosRepository.findAll();
        domicilios.forEach(this::desencriptarDomicilio);
        return domicilios;
    }

    public Optional<Domicilios> obtenerDomicilioPorId(Integer id) {
        Optional<Domicilios> domicilio = domiciliosRepository.findById(id);
        domicilio.ifPresent(this::desencriptarDomicilio);
        return domicilio;
    }

    public Domicilios guardarDomicilio(Domicilios domicilio) {
        encriptarDomicilio(domicilio);
        return domiciliosRepository.save(domicilio);
    }

    public void eliminarDomicilio(Integer id) {
        domiciliosRepository.deleteById(id);
    }

    private void encriptarDomicilio(Domicilios domicilio) {
        try {
            domicilio.setDireccion(encryptionService.encrypt(domicilio.getDireccion()));
            domicilio.setReferencia(encryptionService.encrypt(domicilio.getReferencia()));
            domicilio.setCoordenadas(encryptionService.encrypt(domicilio.getCoordenadas()));
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar datos del domicilio", e);
        }
    }

    private void desencriptarDomicilio(Domicilios domicilio) {
        try {
            domicilio.setDireccion(encryptionService.decrypt(domicilio.getDireccion()));
            domicilio.setReferencia(encryptionService.decrypt(domicilio.getReferencia()));
            domicilio.setCoordenadas(encryptionService.decrypt(domicilio.getCoordenadas()));
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar datos del domicilio", e);
        }
    }
}
