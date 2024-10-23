package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.entities.AnuncioProgramado;
import com.proyecto.servidorpt2.service.AnuncioProgramadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/anuncios-programados")
public class AnuncioProgramadoController {

    @Autowired
    private AnuncioProgramadoService anuncioProgramadoService;

    // Obtener todos los anuncios programados
    @GetMapping
    public ResponseEntity<List<AnuncioProgramado>> obtenerTodosLosAnunciosProgramados() {
        try {
            List<AnuncioProgramado> anunciosProgramados = anuncioProgramadoService.obtenerTodosLosAnunciosProgramados();
            return new ResponseEntity<>(anunciosProgramados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un anuncio programado por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerAnuncioProgramadoPorId(@PathVariable Integer id) {
        try {
            Optional<AnuncioProgramado> anuncioProgramado = anuncioProgramadoService.obtenerAnuncioProgramadoPorId(id);
            if (anuncioProgramado.isPresent()) {
                return new ResponseEntity<>(anuncioProgramado.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Anuncio programado no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener el anuncio programado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo anuncio programado
    @PostMapping
    public ResponseEntity<Object> crearAnuncioProgramado(@RequestBody AnuncioProgramado anuncioProgramado) {
        try {
            AnuncioProgramado nuevoAnuncioProgramado = anuncioProgramadoService.guardarAnuncioProgramado(anuncioProgramado);
            return new ResponseEntity<>(nuevoAnuncioProgramado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el anuncio programado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un anuncio programado existente
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarAnuncioProgramado(@PathVariable Integer id, @RequestBody AnuncioProgramado anuncioProgramado) {
        try {
            Optional<AnuncioProgramado> anuncioProgramadoExistente = anuncioProgramadoService.obtenerAnuncioProgramadoPorId(id);
            if (anuncioProgramadoExistente.isPresent()) {
                anuncioProgramado.setIdMensajeProgramado(id);
                AnuncioProgramado anuncioProgramadoActualizado = anuncioProgramadoService.guardarAnuncioProgramado(anuncioProgramado);
                return new ResponseEntity<>(anuncioProgramadoActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Anuncio programado no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el anuncio programado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un anuncio programado por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarAnuncioProgramado(@PathVariable Integer id) {
        try {
            Optional<AnuncioProgramado> anuncioProgramadoExistente = anuncioProgramadoService.obtenerAnuncioProgramadoPorId(id);
            if (anuncioProgramadoExistente.isPresent()) {
                anuncioProgramadoService.eliminarAnuncioProgramado(id);
                return new ResponseEntity<>("Anuncio programado eliminado con éxito", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Anuncio programado no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el anuncio programado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
