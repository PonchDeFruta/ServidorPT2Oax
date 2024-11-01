package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.dto.AnuncioProgramadoDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.entities.AnuncioProgramado;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.AnuncioProgramadoService;
import com.proyecto.servidorpt2.service.AnuncioService;
import com.proyecto.servidorpt2.service.BroadcastService;
import com.proyecto.servidorpt2.service.ResidentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private AnuncioProgramadoService anuncioProgramadoService;

    @Autowired
    private ResidentesService residentesService;

    @Autowired
    private BroadcastService broadcastService;

    // Obtener un anuncio por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerAnuncioPorId(@PathVariable Integer id) {
        try {
            Optional<Anuncio> anuncio = anuncioService.obtenerAnuncioPorId(id);
            if (anuncio.isPresent()) {
                return new ResponseEntity<>(anuncio.get(), HttpStatus.OK);
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

            // Convertir a DTO
            List<AnuncioDTO> anunciosDTO = anuncios.stream().map(anuncio -> {
                AnuncioDTO dto = new AnuncioDTO();
                dto.setIdMensaje(anuncio.getIdMensaje());
                dto.setTitulo(anuncio.getTitulo());
                dto.setContenidoDelMensaje(anuncio.getContenidoDelMensaje());
                dto.setFechaMensaje(anuncio.getFechaMensaje());
                dto.setEsAudio(anuncio.isEsAudio());
                dto.setIdResidente(anuncio.getResidente() != null ? anuncio.getResidente().getIdResidente() : null);
                return dto;
            }).collect(Collectors.toList());

            return new ResponseEntity<>(anunciosDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener los anuncios: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo anuncio
    @PostMapping("crearAnuncio")
    public ResponseEntity<ApiResponse> crearAnuncio(@RequestBody AnuncioDTO anuncioDTO) {
        try {
            Anuncio nuevoAnuncio = new Anuncio();
            nuevoAnuncio.setTitulo(anuncioDTO.getTitulo());
            nuevoAnuncio.setContenidoDelMensaje(anuncioDTO.getContenidoDelMensaje());
            nuevoAnuncio.setFechaMensaje(anuncioDTO.getFechaMensaje());
            nuevoAnuncio.setEsAudio(anuncioDTO.isEsAudio());

            // Manejar la relación con el residente si se proporciona un ID
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

            // Enviar el anuncio por broadcast UDP
            broadcastService.enviarAnuncioPorBroadcast(anuncioGuardado);

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
                anuncio.setFechaMensaje(anuncioDTO.getFechaMensaje());
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

    // Crear y programar un anuncio
    @PostMapping("/programar")
    public ResponseEntity<ApiResponse> crearYProgramarAnuncio(@RequestBody AnuncioProgramadoDTO anuncioDTO) {
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

            return new ResponseEntity<>(new ApiResponse("success", "Anuncio creado y programado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear o programar el anuncio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
