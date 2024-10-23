package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.dto.AnuncioProgramadoDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.entities.AnuncioProgramado;
import com.proyecto.servidorpt2.service.AnuncioProgramadoService;
import com.proyecto.servidorpt2.service.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private AnuncioProgramadoService anuncioProgramadoService;

    // Obtener todos los anuncios
    @GetMapping
    public ResponseEntity<List<Anuncio>> obtenerTodosLosAnuncios() {
        try {
            List<Anuncio> anuncios = anuncioService.obtenerTodosLosAnuncios();
            return new ResponseEntity<>(anuncios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un anuncio por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerAnuncioPorId(@PathVariable Integer id) {
        try {
            Optional<Anuncio> anuncio = anuncioService.obtenerAnuncioPorId(id);
            if (anuncio.isPresent()) {
                return new ResponseEntity<>(anuncio.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Anuncio no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener el anuncio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo anuncio
    @PostMapping
    public ResponseEntity<Object> crearAnuncio(@RequestBody Anuncio anuncio) {
        try {
            Anuncio nuevoAnuncio = anuncioService.guardarAnuncio(anuncio);
            return new ResponseEntity<>(nuevoAnuncio, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear el anuncio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un anuncio existente
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarAnuncio(@PathVariable Integer id, @RequestBody Anuncio anuncio) {
        try {
            Optional<Anuncio> anuncioExistente = anuncioService.obtenerAnuncioPorId(id);
            if (anuncioExistente.isPresent()) {
                anuncio.setIdMensaje(id);
                Anuncio anuncioActualizado = anuncioService.guardarAnuncio(anuncio);
                return new ResponseEntity<>(anuncioActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Anuncio no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el anuncio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un anuncio por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarAnuncio(@PathVariable Integer id) {
        try {
            Optional<Anuncio> anuncioExistente = anuncioService.obtenerAnuncioPorId(id);
            if (anuncioExistente.isPresent()) {
                anuncioService.eliminarAnuncio(id);
                return new ResponseEntity<>("Anuncio eliminado con éxito", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Anuncio no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el anuncio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear y programar un anuncio
    @PostMapping("/programar")
    public ResponseEntity<Object> crearYProgramarAnuncio(@RequestBody AnuncioProgramadoDTO anuncioDTO) {
        try {
            // Crear el anuncio
            Anuncio nuevoAnuncio = new Anuncio();
            nuevoAnuncio.setContenidoDelMensaje(anuncioDTO.getContenidoDelMensaje());
            nuevoAnuncio.setFechaMensaje(anuncioDTO.getFechaMensaje() != null ? anuncioDTO.getFechaMensaje() : LocalDateTime.now());
            Anuncio anuncioGuardado = anuncioService.guardarAnuncio(nuevoAnuncio);

            // Si hay una fecha programada, crear un anuncio programado
            if (anuncioDTO.getFechaProgramada() != null && anuncioDTO.getFechaProgramada().isAfter(LocalDateTime.now())) {
                AnuncioProgramado anuncioProgramado = new AnuncioProgramado();
                anuncioProgramado.setAnuncio(anuncioGuardado);
                anuncioProgramado.setFechaMensajeProgramado(anuncioDTO.getFechaProgramada());
                anuncioProgramado.setEstatusMsj("PENDIENTE");

                // Guardar el anuncio programado
                anuncioProgramadoService.guardarAnuncioProgramado(anuncioProgramado);
            }

            return new ResponseEntity<>(anuncioGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear o programar el anuncio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
