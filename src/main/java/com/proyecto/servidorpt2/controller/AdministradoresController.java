package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.entities.Administradores;
import com.proyecto.servidorpt2.service.AdministradoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administradores")
public class AdministradoresController {

    @Autowired
    private AdministradoresService administradoresService;

    // GET: Obtener todos los administradores
    @GetMapping
    public ResponseEntity<ApiResponse> obtenerTodosLosAdministradores() {
        try {
            List<Administradores> administradores = administradoresService.obtenerTodosLosAdministradores();
            return new ResponseEntity<>(new ApiResponse("success", "Administradores obtenidos con éxito"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener los administradores"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Obtener un administrador por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtenerAdministradorPorId(@PathVariable Integer id) {
        try {
            Optional<Administradores> administrador = administradoresService.obtenerAdministradorPorId(id);
            if (administrador.isPresent()) {
                return new ResponseEntity<>(new ApiResponse("success", "Administrador encontrado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Administrador no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el administrador"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Crear un nuevo administrador
    @PostMapping
    public ResponseEntity<ApiResponse> crearAdministrador(@RequestBody Administradores administrador) {
        try {
            administradoresService.guardarAdministrador(administrador);
            return new ResponseEntity<>(new ApiResponse("success", "Administrador creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el administrador"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT: Actualizar un administrador existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarAdministrador(@PathVariable Integer id, @RequestBody Administradores administrador) {
        try {
            Optional<Administradores> administradorExistente = administradoresService.obtenerAdministradorPorId(id);
            if (administradorExistente.isPresent()) {
                administrador.setIdAdministrador(id); // Asegurar que el ID es el correcto
                administradoresService.guardarAdministrador(administrador);
                return new ResponseEntity<>(new ApiResponse("success", "Administrador actualizado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Administrador no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el administrador"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE: Eliminar un administrador por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarAdministrador(@PathVariable Integer id) {
        try {
            Optional<Administradores> administradorExistente = administradoresService.obtenerAdministradorPorId(id);
            if (administradorExistente.isPresent()) {
                administradoresService.eliminarAdministrador(id);
                return new ResponseEntity<>(new ApiResponse("success", "Administrador eliminado con éxito"), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Administrador no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el administrador"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
