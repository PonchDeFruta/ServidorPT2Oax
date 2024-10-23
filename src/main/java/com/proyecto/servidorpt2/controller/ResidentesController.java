package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.DomiciliosService;
import com.proyecto.servidorpt2.service.ResidentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/residentes")
public class ResidentesController {

    @Autowired
    private ResidentesService residentesService;

    @Autowired
    private DomiciliosService domiciliosService;

    // Obtener todos los residentes
    @GetMapping
    public List<Residentes> obtenerTodosLosResidentes() {
        return residentesService.obtenerTodosLosResidentes();
    }

    // Obtener un residente por su ID
    @GetMapping("/{id}")
    public Optional<Residentes> obtenerResidentePorId(@PathVariable Integer id) {
        return residentesService.obtenerResidentePorId(id);
    }

    // Crear un nuevo residente
    @PostMapping
    public Residentes crearResidente(@RequestBody Residentes residente) {
        return residentesService.guardarResidente(residente);
    }

    // Actualizar un residente existente
    @PutMapping("/{id}")
    public Residentes actualizarResidente(@PathVariable Integer id, @RequestBody Residentes residente) {
        residente.setIdResidente(id);
        return residentesService.guardarResidente(residente);
    }

    // Eliminar un residente por su ID
    @DeleteMapping("/{id}")
    public void eliminarResidente(@PathVariable Integer id) {
        residentesService.eliminarResidente(id);
    }

    // Registrar un residente con un domicilio existente o nuevo
    @PostMapping("/registrar")
    public ResponseEntity<Residentes> registrarResidente(@RequestBody ResidenteDTO residenteDTO) {
        Domicilios domicilio;

        // Si se proporciona un domicilioId, buscar el domicilio existente
        if (residenteDTO.getDomicilioId() != null) {
            Optional<Domicilios> domicilioExistente = domiciliosService.obtenerDomicilioPorId(residenteDTO.getDomicilioId());
            if (!domicilioExistente.isPresent()) {
                return ResponseEntity.badRequest().body(null); // Error si no existe
            }
            domicilio = domicilioExistente.get();
        } else {
            // Crear un nuevo domicilio si no se proporcionó un domicilioId
            Domicilios nuevoDomicilio = new Domicilios();
            nuevoDomicilio.setDireccion(residenteDTO.getDireccion());
            nuevoDomicilio.setReferencia(residenteDTO.getReferencia());
            nuevoDomicilio.setCoordenadas(residenteDTO.getCoordenadas());
            domicilio = domiciliosService.guardarDomicilio(nuevoDomicilio); // Guardar nuevo domicilio
        }

        // Crear el nuevo residente y asociarlo al domicilio
        Residentes nuevoResidente = new Residentes();
        nuevoResidente.setNombre(residenteDTO.getNombre());
        nuevoResidente.setApellido(residenteDTO.getApellido());
        nuevoResidente.setApodo(residenteDTO.getApodo());
        nuevoResidente.setComercio(residenteDTO.getComercio());
        nuevoResidente.setDomicilio(domicilio);

        // Guardar el residente en la base de datos
        Residentes residenteGuardado = residentesService.guardarResidente(nuevoResidente);

        return ResponseEntity.ok(residenteGuardado);
    }
}
