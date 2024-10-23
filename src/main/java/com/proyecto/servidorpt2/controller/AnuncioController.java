package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.dto.AnuncioProgramadoDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.entities.AnuncioProgramado;
import com.proyecto.servidorpt2.service.AnuncioProgramadoService;
import com.proyecto.servidorpt2.service.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Anuncio> obtenerTodosLosAnuncios() {
        return anuncioService.obtenerTodosLosAnuncios();
    }

    // Obtener un anuncio por su ID
    @GetMapping("/{id}")
    public Optional<Anuncio> obtenerAnuncioPorId(@PathVariable Integer id) {
        return anuncioService.obtenerAnuncioPorId(id);
    }

    // Crear un nuevo anuncio
    @PostMapping
    public Anuncio crearAnuncio(@RequestBody Anuncio anuncio) {
        return anuncioService.guardarAnuncio(anuncio);
    }

    // Actualizar un anuncio existente
    @PutMapping("/{id}")
    public Anuncio actualizarAnuncio(@PathVariable Integer id, @RequestBody Anuncio anuncio) {
        anuncio.setIdMensaje(id);
        return anuncioService.guardarAnuncio(anuncio);
    }

    // Eliminar un anuncio por su ID
    @DeleteMapping("/{id}")
    public void eliminarAnuncio(@PathVariable Integer id) {
        anuncioService.eliminarAnuncio(id);
    }

    // Crear y programar un anuncio
    @PostMapping("/programar")
    public ResponseEntity<Anuncio> crearYProgramarAnuncio(@RequestBody AnuncioProgramadoDTO anuncioDTO) {
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

        return ResponseEntity.ok(anuncioGuardado);
    }
}
