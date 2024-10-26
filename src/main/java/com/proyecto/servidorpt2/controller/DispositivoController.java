package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.entities.Dispositivo;
import com.proyecto.servidorpt2.service.DispositivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {

    @Autowired
    private DispositivoService dispositivoService;

    // GET: Obtener todos los dispositivos
    @GetMapping
    public ResponseEntity<ApiResponse> obtenerTodosLosDispositivos() {
        try {
            List<Dispositivo> dispositivos = dispositivoService.obtenerTodosLosDispositivos();
            return new ResponseEntity<>(new ApiResponse("success", "Dispositivos obtenidos con éxito"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener la lista de dispositivos"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Obtener un dispositivo por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtenerDispositivoPorId(@PathVariable Integer id) {
        try {
            Optional<Dispositivo> dispositivoData = dispositivoService.obtenerDispositivoPorId(id);
            if (dispositivoData.isPresent()) {
                return new ResponseEntity<>(new ApiResponse("success", "Dispositivo encontrado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Dispositivo no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el dispositivo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Crear un nuevo dispositivo
    @PostMapping
    public ResponseEntity<ApiResponse> crearDispositivo(@RequestBody Dispositivo dispositivo) {
        try {
            dispositivoService.guardarDispositivo(dispositivo);
            return new ResponseEntity<>(new ApiResponse("success", "Dispositivo creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el dispositivo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT: Actualizar un dispositivo existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarDispositivo(@PathVariable Integer id, @RequestBody Dispositivo dispositivo) {
        try {
            Optional<Dispositivo> dispositivoExistente = dispositivoService.obtenerDispositivoPorId(id);
            if (dispositivoExistente.isPresent()) {
                dispositivo.setIdDispositivo(id); // Asegurarse de que el ID sea el correcto
                dispositivoService.guardarDispositivo(dispositivo);
                return new ResponseEntity<>(new ApiResponse("success", "Dispositivo actualizado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Dispositivo no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el dispositivo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE: Eliminar un dispositivo por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarDispositivo(@PathVariable Integer id) {
        try {
            Optional<Dispositivo> dispositivoExistente = dispositivoService.obtenerDispositivoPorId(id);
            if (dispositivoExistente.isPresent()) {
                dispositivoService.eliminarDispositivo(id);
                return new ResponseEntity<>(new ApiResponse("success", "Dispositivo eliminado con éxito"), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Dispositivo no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el dispositivo"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
