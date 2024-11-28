package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.dto.DomicilioDTO;
import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.AnuncioService;
import com.proyecto.servidorpt2.service.BroadcastService;
import com.proyecto.servidorpt2.service.DomiciliosService;
import com.proyecto.servidorpt2.service.ResidentesService;
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

    @Autowired
    private DomiciliosService domiciliosService;

    @Autowired
    private ResidentesService residentesService;

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
            // Guardar el anuncio cifrado en la base de datos
            AnuncioDTO anuncioGuardadoDTO = anuncioService.guardarAnuncioConCifrado(anuncioDTO);

            // Validar si el anuncio tiene un residente asociado
            if (anuncioDTO.getIdResidente() != null) {
                Optional<Residentes> residenteOpt = residentesService.obtenerResidentePorId(anuncioDTO.getIdResidente());
                if (residenteOpt.isPresent()) {
                    Residentes residente = residenteOpt.get();

                    // Construir el ResidenteDTO con datos descifrados
                    ResidenteDTO residenteDTO = new ResidenteDTO();
                    residenteDTO.setIdResidente(residente.getIdResidente());
                    residenteDTO.setNombre(residente.getNombre()); // Descifrar nombre
                    residenteDTO.setApellido(residente.getApellido()); // Descifrar apellido
                    residenteDTO.setApodo(residente.getApodo());
                    residenteDTO.setComercio(residente.getComercio());

                    // Validar si el residente tiene un domicilio asociado
                    if (residente.getDomicilio() != null) {
                        Optional<Domicilios> domicilioOpt = domiciliosService.obtenerDomicilioPorId(residente.getDomicilio().getIdDomicilio());
                        if (domicilioOpt.isPresent()) {

                            // Construir el DomicilioDTO con datos descifrados
                            DomicilioDTO domicilioDTO = new DomicilioDTO();
                            domicilioDTO.setIdDomicilio(domicilioOpt.get().getIdDomicilio());
                            domicilioDTO.setDireccion(domicilioOpt.get().getDireccion());
                            domicilioDTO.setReferencia(domicilioOpt.get().getReferencia());
                            domicilioDTO.setCoordenadas(domicilioOpt.get().getCoordenadas());

                            residenteDTO.setDomicilio(domicilioDTO);
                        }
                    }

                    anuncioGuardadoDTO.setResidente(residenteDTO); // Asignar residente descifrado al DTO
                }
            }

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
