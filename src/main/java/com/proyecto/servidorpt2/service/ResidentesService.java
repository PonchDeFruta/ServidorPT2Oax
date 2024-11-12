package com.proyecto.servidorpt2.service;

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

    // Obtener todos los residentes
    public List<Residentes> obtenerTodosLosResidentes() {
        return residentesRepository.findAll();
    }

    // Obtener un residente por su ID
    public Optional<Residentes> obtenerResidentePorId(Integer id) {
        return residentesRepository.findById(id);
    }

    public Residentes guardarResidente(Residentes residente) {
        return residentesRepository.save(residente);
    }

    //NO FALTA UN METODO PARA GUARDAR LOS RESIDENTES CON DOMICILIO ??

    public void eliminarResidente(Integer id) {
        residentesRepository.deleteById(id);
    }
    public List<Residentes> obtenerResidentesConDomicilio() {
        // Usamos una consulta personalizada para obtener solo los residentes con domicilio
        return residentesRepository.findResidentesWithDomicilio();
    }
}
