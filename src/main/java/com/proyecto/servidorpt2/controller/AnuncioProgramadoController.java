package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
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
    public ResponseEntity<Object> obtenerTodosLosAnunciosProgramados() {
        try {
            List<AnuncioProgramado> anunciosProgramados = anuncioProgramadoService.obtenerTodosLosAnunciosProgramados();
            return new ResponseEntity<>(anunciosProgramados, HttpStatus.OK); // Devuelve la lista de anuncios directamente
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener los anuncios programados: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un anuncio programado por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerAnuncioProgramadoPorId(@PathVariable Integer id) {
        try {
            Optional<AnuncioProgramado> anuncioProgramado = anuncioProgramadoService.obtenerAnuncioProgramadoPorId(id);
            if (anuncioProgramado.isPresent()) {
                return new ResponseEntity<>(anuncioProgramado.get(), HttpStatus.OK); // Devuelve el anuncio programado directamente
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Anuncio programado no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el anuncio programado: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo anuncio programado
    @PostMapping
    public ResponseEntity<ApiResponse> crearAnuncioProgramado(@RequestBody AnuncioProgramado anuncioProgramado) {
        try {
            anuncioProgramadoService.guardarAnuncioProgramado(anuncioProgramado);
            return new ResponseEntity<>(new ApiResponse("success", "Anuncio programado creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el anuncio programado: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un anuncio programado existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarAnuncioProgramado(@PathVariable Integer id, @RequestBody AnuncioProgramado anuncioProgramado) {
        try {
            Optional<AnuncioProgramado> anuncioProgramadoExistente = anuncioProgramadoService.obtenerAnuncioProgramadoPorId(id);
            if (anuncioProgramadoExistente.isPresent()) {
                anuncioProgramado.setIdMensajeProgramado(id);
                anuncioProgramadoService.guardarAnuncioProgramado(anuncioProgramado);
                return new ResponseEntity<>(new ApiResponse("success", "Anuncio programado actualizado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Anuncio programado no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el anuncio programado: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un anuncio programado por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarAnuncioProgramado(@PathVariable Integer id) {
        try {
            Optional<AnuncioProgramado> anuncioProgramadoExistente = anuncioProgramadoService.obtenerAnuncioProgramadoPorId(id);
            if (anuncioProgramadoExistente.isPresent()) {
                anuncioProgramadoService.eliminarAnuncioProgramado(id);
                return new ResponseEntity<>(new ApiResponse("success", "Anuncio programado eliminado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Anuncio programado no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el anuncio programado: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
