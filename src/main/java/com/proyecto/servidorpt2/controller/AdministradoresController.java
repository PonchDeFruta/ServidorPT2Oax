package com.proyecto.servidorpt2.controller;

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
    public ResponseEntity<List<Administradores>> obtenerTodosLosAdministradores() {
        try {
            List<Administradores> administradores = administradoresService.obtenerTodosLosAdministradores();
            return new ResponseEntity<>(administradores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Obtener un administrador por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerAdministradorPorId(@PathVariable Integer id) {
        try {
            Optional<Administradores> administrador = administradoresService.obtenerAdministradorPorId(id);
            if (administrador.isPresent()) {
                return new ResponseEntity<>(administrador.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Administrador no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Crear un nuevo administrador
    @PostMapping
    public ResponseEntity<Object> crearAdministrador(@RequestBody Administradores administrador) {
        try {
            Administradores nuevoAdministrador = administradoresService.guardarAdministrador(administrador);
            return new ResponseEntity<>(nuevoAdministrador, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT: Actualizar un administrador existente
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarAdministrador(@PathVariable Integer id, @RequestBody Administradores administrador) {
        try {
            Optional<Administradores> administradorExistente = administradoresService.obtenerAdministradorPorId(id);
            if (administradorExistente.isPresent()) {
                administrador.setIdAdministrador(id); // Asegurarse de que el ID es el correcto
                Administradores administradorActualizado = administradoresService.guardarAdministrador(administrador);
                return new ResponseEntity<>(administradorActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Administrador no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE: Eliminar un administrador por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarAdministrador(@PathVariable Integer id) {
        try {
            Optional<Administradores> administradorExistente = administradoresService.obtenerAdministradorPorId(id);
            if (administradorExistente.isPresent()) {
                administradoresService.eliminarAdministrador(id);
                return new ResponseEntity<>("Administrador eliminado con éxito", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Administrador no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el administrador", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
