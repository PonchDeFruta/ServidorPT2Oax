package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.service.DomiciliosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/domicilios")
public class DomiciliosController {

    @Autowired
    private DomiciliosService domiciliosService;

    // Obtener todos los domicilios
    @GetMapping("/obtenerTodosLosDomicilios")
    public ResponseEntity<Object> obtenerTodosLosDomicilios() {
        try {
            List<Domicilios> domicilios = domiciliosService.obtenerTodosLosDomicilios();
            return new ResponseEntity<>(domicilios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener la lista de domicilios: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un domicilio por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerDomicilioPorId(@PathVariable Integer id) {
        try {
            Optional<Domicilios> domicilio = domiciliosService.obtenerDomicilioPorId(id);
            if (domicilio.isPresent()) {
                return new ResponseEntity<>(domicilio.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Domicilio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el domicilio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo domicilio
    @PostMapping("/crearDomicilio")
    public ResponseEntity<ApiResponse> crearDomicilio(@RequestBody Domicilios domicilio) {
        try {
            domiciliosService.guardarDomicilio(domicilio);
            return new ResponseEntity<>(new ApiResponse("success", "Domicilio creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el domicilio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un domicilio existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarDomicilio(@PathVariable Integer id, @RequestBody Domicilios domicilio) {
        try {
            Optional<Domicilios> domicilioExistente = domiciliosService.obtenerDomicilioPorId(id);
            if (domicilioExistente.isPresent()) {
                domicilio.setIdDomicilio(id);
                domiciliosService.guardarDomicilio(domicilio);
                return new ResponseEntity<>(new ApiResponse("success", "Domicilio actualizado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Domicilio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el domicilio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un domicilio por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarDomicilio(@PathVariable Integer id) {
        try {
            Optional<Domicilios> domicilioExistente = domiciliosService.obtenerDomicilioPorId(id);
            if (domicilioExistente.isPresent()) {
                domiciliosService.eliminarDomicilio(id);
                return new ResponseEntity<>(new ApiResponse("success", "Domicilio eliminado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Domicilio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el domicilio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
