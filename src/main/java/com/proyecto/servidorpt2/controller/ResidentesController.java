
package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.DomiciliosService;
import com.proyecto.servidorpt2.service.ResidentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileDescriptor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/residentes")
public class ResidentesController {

    @Autowired
    private ResidentesService residentesService;
    @Autowired
    DomiciliosService domiciliosService;

    // Obtener todos los residentes
    @GetMapping("/obtenerResidenteDomicilio")
    public ResponseEntity<Object> obtenerTodosLosResidentesConDomicilio() {
        try {
            // Obtener los residentes con domicilio, asegurándonos de que tengan domicilio
            List<Residentes> residentes = residentesService.obtenerResidentesConDomicilio();

            // Filtrar los residentes que no tienen domicilio
            residentes = residentes.stream()
                    .filter(residente -> residente.getDomicilio() != null)  // Filtramos los residentes sin domicilio
                    .collect(Collectors.toList());

            return new ResponseEntity<>(residentes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener la lista de residentes con domicilio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo residente

    @PostMapping("/crearResidente")
    public ResponseEntity<ApiResponse> crearResidente(@RequestBody Residentes residente) {
        try {
            // Verificar si el residente trae un domicilio
            if (residente.getDomicilio() != null) {
                Domicilios domicilio = residente.getDomicilio();
                if (domicilio.getDireccion() != null &&!domicilio.getDireccion().isEmpty()) {
                    // Guardar el domicilio primero

                    domicilio = domiciliosService.guardarDomicilio(domicilio);
                    residente.setDomicilio(domicilio);
                } else {
                    return new ResponseEntity<>(new ApiResponse("error", "El domicilio proporcionado está incompleto"), HttpStatus.BAD_REQUEST);
                }
            }
            System.out.println("Residente: " + residente);
            // Guardar el residente
            residentesService.guardarResidente(residente);
            return new ResponseEntity<>(new ApiResponse("success", "Residente creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un residente existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarResidente(@PathVariable Integer id, @RequestBody Residentes residente) {
        try {
            Optional<Residentes> residenteExistente = residentesService.obtenerResidentePorId(id);
            if (residenteExistente.isPresent()) {
                residente.setIdResidente(id);
                residentesService.guardarResidente(residente);
                return new ResponseEntity<>(new ApiResponse("success", "Residente actualizado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un residente por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarResidente(@PathVariable Integer id) {
        try {
            Optional<Residentes> residenteExistente = residentesService.obtenerResidentePorId(id);
            if (residenteExistente.isPresent()) {
                residentesService.eliminarResidente(id);
                return new ResponseEntity<>(new ApiResponse("success", "Residente eliminado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
