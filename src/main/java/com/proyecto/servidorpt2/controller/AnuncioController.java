package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.dto.DomicilioDTO;
import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.AnuncioService;
import com.proyecto.servidorpt2.service.BroadcastService;
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
    private ResidentesService residentesService;

    @Autowired
    private BroadcastService broadcastService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Obtener un anuncio por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerAnuncioPorId(@PathVariable Integer id) {
        try {
            Optional<Anuncio> anuncio = anuncioService.obtenerAnuncioPorId(id);
            if (anuncio.isPresent()) {
                AnuncioDTO dto = anuncioService.convertirAnuncioAAnuncioDTO(anuncio.get());
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Anuncio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los anuncios
    @GetMapping("/todos")
    public ResponseEntity<Object> obtenerTodosLosAnuncios() {
        try {
            List<Anuncio> anuncios = anuncioService.obtenerTodosLosAnuncios();
            if (anuncios.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("info", "No hay anuncios disponibles"), HttpStatus.OK);
            }

            List<AnuncioDTO> anunciosDTO = anuncios.stream()
                    .map(anuncio -> anuncioService.convertirAnuncioAAnuncioDTO(anuncio))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(anunciosDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener los anuncios: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/hoy")
    public ResponseEntity<Object> obtenerAnunciosDeHoy() {
        try {
            List<Anuncio> anuncios = anuncioService.obtenerTodosLosAnuncios();
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

            List<AnuncioDTO> anunciosDeHoyDTO = anuncios.stream()
                    .filter(anuncio -> anuncio.getFechaMensaje().isAfter(startOfDay) && anuncio.getFechaMensaje().isBefore(endOfDay))
                    .map(anuncio -> anuncioService.convertirAnuncioAAnuncioDTO(anuncio))
                    .collect(Collectors.toList());

            if (anunciosDeHoyDTO.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("info", "No hay anuncios para el día de hoy"), HttpStatus.OK);
            } else if (anunciosDeHoyDTO.size() == 1) {
                return new ResponseEntity<>(anunciosDeHoyDTO.get(0), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(anunciosDeHoyDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener los anuncios de hoy: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Crear un nuevo anuncio

    @PostMapping("/crearAnuncio")
    public ResponseEntity<ApiResponse> crearAnuncio(@RequestBody AnuncioDTO anuncioDTO) {
        try {
            Anuncio nuevoAnuncio = new Anuncio();
            nuevoAnuncio.setTitulo(anuncioDTO.getTitulo());
            nuevoAnuncio.setContenidoDelMensaje(anuncioDTO.getContenidoDelMensaje());
            nuevoAnuncio.setFechaMensaje(LocalDateTime.parse(anuncioDTO.getFechaMensaje()));
            nuevoAnuncio.setEsAudio(anuncioDTO.isEsAudio());

            // Verificar si se proporciona un ID de residente
            if (anuncioDTO.getIdResidente() != null) {
                Optional<Residentes> residenteOpt = residentesService.obtenerResidentePorId(anuncioDTO.getIdResidente());
                if (residenteOpt.isPresent()) {
                    nuevoAnuncio.setResidente(residenteOpt.get());
                } else {
                    return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + anuncioDTO.getIdResidente()), HttpStatus.BAD_REQUEST);
                }
            }

            // Guardar el anuncio
            Anuncio anuncioGuardado = anuncioService.guardarAnuncio(nuevoAnuncio);

            // Convertir a AnuncioDTO para enviar y devolver en el formato requerido
            AnuncioDTO anuncioDTOResponse = anuncioService.convertirAnuncioAAnuncioDTO(anuncioGuardado);

            // Enviar el anuncio por broadcast UDP en formato JSON
            broadcastService.enviarAnuncioPorBroadcast(anuncioDTOResponse);

            return new ResponseEntity<>(new ApiResponse("success", "Anuncio creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Actualizar un anuncio existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarAnuncio(@PathVariable Integer id, @RequestBody AnuncioDTO anuncioDTO) {
        try {
            Optional<Anuncio> anuncioExistente = anuncioService.obtenerAnuncioPorId(id);
            if (anuncioExistente.isPresent()) {
                Anuncio anuncio = anuncioExistente.get();
                anuncio.setTitulo(anuncioDTO.getTitulo());
                anuncio.setContenidoDelMensaje(anuncioDTO.getContenidoDelMensaje());
                anuncio.setFechaMensaje(anuncioDTO.getFechaMensaje() != null ? LocalDateTime.parse(anuncioDTO.getFechaMensaje(), formatter) : LocalDateTime.now());
                anuncio.setEsAudio(anuncioDTO.isEsAudio());

                // Actualizar la relación con el residente si se proporciona un ID
                if (anuncioDTO.getIdResidente() != null) {
                    Optional<Residentes> residenteOpt = residentesService.obtenerResidentePorId(anuncioDTO.getIdResidente());
                    if (residenteOpt.isPresent()) {
                        anuncio.setResidente(residenteOpt.get());
                    } else {
                        return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + anuncioDTO.getIdResidente()), HttpStatus.BAD_REQUEST);
                    }
                }

                anuncioService.guardarAnuncio(anuncio);
                return new ResponseEntity<>(new ApiResponse("success", "Anuncio actualizado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Anuncio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un anuncio por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarAnuncio(@PathVariable Integer id) {
        try {
            Optional<Anuncio> anuncioExistente = anuncioService.obtenerAnuncioPorId(id);
            if (anuncioExistente.isPresent()) {
                anuncioService.eliminarAnuncio(id);
                return new ResponseEntity<>(new ApiResponse("success", "Anuncio eliminado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Anuncio no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/borrarTodos")
    public ResponseEntity<ApiResponse> borrarTodosLosAnuncios() {
        try {
            anuncioService.eliminarTodosLosAnuncios(); // Implementa este método en tu servicio
            return new ResponseEntity<>(new ApiResponse("success", "Todos los anuncios han sido borrados"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al borrar los anuncios: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}