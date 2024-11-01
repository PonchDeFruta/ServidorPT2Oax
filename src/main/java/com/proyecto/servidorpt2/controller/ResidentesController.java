
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/residentes")
public class ResidentesController {

    @Autowired
    private ResidentesService residentesService;

    // Obtener todos los residentes
    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosResidentes() {
        try {
            List<Residentes> residentes = residentesService.obtenerTodosLosResidentes();
            return new ResponseEntity<>(residentes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener la lista de residentes: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
    @PostMapping("/crearResidenteConDomicilio")
    public ResponseEntity<ApiResponse> crearResidenteConDomicilio(@RequestBody Residentes residente) {
        try {
            // Verificar si el residente trae un domicilio
            if (residente.getDomicilio() != null) {
                // Si el domicilio tiene valores válidos (e.g., dirección), guardar el domicilio
                Domicilios domicilio = residente.getDomicilio();
                if (domicilio.getDireccion() != null && !domicilio.getDireccion().isEmpty()) {
                    // Guardar el domicilio en cascada junto con el residente
                    residente.setDomicilio(domicilio);
                } else {
                    // Si el domicilio está incompleto, devolver un error
                    return new ResponseEntity<>(new ApiResponse("error", "El domicilio proporcionado está incompleto"), HttpStatus.BAD_REQUEST);
                }
            }

            // Guardar el residente (con o sin domicilio)
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
