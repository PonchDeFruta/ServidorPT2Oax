package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.DomiciliosService;
import com.proyecto.servidorpt2.service.ResidentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> obtenerTodosLosResidentes() {
        try {
            List<Residentes> residentes = residentesService.obtenerTodosLosResidentes();
            return new ResponseEntity<>(residentes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener la lista de residentes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un residente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerResidentePorId(@PathVariable Integer id) {
        try {
            Optional<Residentes> residente = residentesService.obtenerResidentePorId(id);
            if (residente.isPresent()) {
                return new ResponseEntity<>(residente.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Residente no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener el residente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo residente
    @PostMapping
    public ResponseEntity<Object> crearResidente(@RequestBody Residentes residente) {
        try {
            Residentes nuevoResidente = residentesService.guardarResidente(residente);
            return new ResponseEntity<>(nuevoResidente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el residente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un residente existente
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarResidente(@PathVariable Integer id, @RequestBody Residentes residente) {
        try {
            Optional<Residentes> residenteExistente = residentesService.obtenerResidentePorId(id);
            if (residenteExistente.isPresent()) {
                residente.setIdResidente(id);
                Residentes residenteActualizado = residentesService.guardarResidente(residente);
                return new ResponseEntity<>(residenteActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Residente no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el residente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un residente por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarResidente(@PathVariable Integer id) {
        try {
            Optional<Residentes> residenteExistente = residentesService.obtenerResidentePorId(id);
            if (residenteExistente.isPresent()) {
                residentesService.eliminarResidente(id);
                return new ResponseEntity<>("Residente eliminado con éxito", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Residente no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el residente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Registrar un residente con un domicilio existente o nuevo
    @PostMapping("/registrar")
    public ResponseEntity<Object> registrarResidente(@RequestBody ResidenteDTO residenteDTO) {
        try {
            Domicilios domicilio;

            // Si se proporciona un domicilioId, buscar el domicilio existente
            if (residenteDTO.getDomicilioId() != null) {
                Optional<Domicilios> domicilioExistente = domiciliosService.obtenerDomicilioPorId(residenteDTO.getDomicilioId());
                if (!domicilioExistente.isPresent()) {
                    return new ResponseEntity<>("Domicilio no encontrado con ID: " + residenteDTO.getDomicilioId(), HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(residenteGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar el residente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
