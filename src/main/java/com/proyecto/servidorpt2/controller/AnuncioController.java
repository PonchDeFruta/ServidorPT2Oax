package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.service.AnuncioService;
import com.proyecto.servidorpt2.service.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private BroadcastService broadcastService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAnuncioPorId(@PathVariable Integer id) {
        try {
            Optional<AnuncioDTO> anuncio = anuncioService.obtenerAnuncioDescifradoPorId(id);
            return anuncio
                    .<ResponseEntity<?>>map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(new ApiResponse("error", "Anuncio no encontrado con ID: " + id), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/todos")
    public ResponseEntity<Object> obtenerTodosLosAnuncios() {
        try {
            List<AnuncioDTO> anunciosDTO = anuncioService.obtenerTodosLosAnunciosDescifrados();
            if (anunciosDTO.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("info", "No hay anuncios disponibles"), HttpStatus.OK);
            }
            return new ResponseEntity<>(anunciosDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener los anuncios: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hoy")
    public ResponseEntity<Object> obtenerAnunciosDeHoy() {
        try {
            List<AnuncioDTO> anunciosDeHoyDTO = anuncioService.obtenerTodosLosAnunciosDescifrados().stream()
                    .filter(anuncio -> {
                        LocalDateTime fechaMensaje = LocalDateTime.parse(anuncio.getFechaMensaje(), formatter);
                        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
                        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
                        return fechaMensaje.isAfter(startOfDay) && fechaMensaje.isBefore(endOfDay);
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(anunciosDeHoyDTO.isEmpty() ? new ApiResponse("info", "No hay anuncios para el día de hoy") : anunciosDeHoyDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener los anuncios de hoy: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/crearAnuncio")
    public ResponseEntity<ApiResponse> crearAnuncio(@RequestBody AnuncioDTO anuncioDTO) {
        try {
            AnuncioDTO anuncioGuardadoDTO = anuncioService.guardarAnuncioConCifrado(anuncioDTO);

            // Enviar el anuncio por broadcast UDP en formato JSON
            broadcastService.enviarAnuncioPorBroadcast(anuncioGuardadoDTO);

            return new ResponseEntity<>(new ApiResponse("success", "Anuncio creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarAnuncio(@PathVariable Integer id, @RequestBody AnuncioDTO anuncioDTO) {
        try {
            if (anuncioService.obtenerAnuncioDescifradoPorId(id).isPresent()) {
                anuncioDTO.setIdMensaje(id); // Asegura que el id se respete durante la actualización
                anuncioService.guardarAnuncioConCifrado(anuncioDTO);
                return new ResponseEntity<>(new ApiResponse("success", "Anuncio actualizado con éxito"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse("error", "Anuncio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarAnuncio(@PathVariable Integer id) {
        try {
            if (anuncioService.obtenerAnuncioDescifradoPorId(id).isPresent()) {
                anuncioService.eliminarAnuncio(id);
                return new ResponseEntity<>(new ApiResponse("success", "Anuncio eliminado con éxito"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse("error", "Anuncio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/borrarTodos")
    public ResponseEntity<ApiResponse> borrarTodosLosAnuncios() {
        try {
            anuncioService.eliminarTodosLosAnuncios();
            return new ResponseEntity<>(new ApiResponse("success", "Todos los anuncios han sido borrados"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al borrar los anuncios: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
