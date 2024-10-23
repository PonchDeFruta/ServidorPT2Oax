package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.entities.AnuncioProgramado;
import com.proyecto.servidorpt2.service.AnuncioProgramadoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<AnuncioProgramado> obtenerTodosLosAnunciosProgramados() {
        return anuncioProgramadoService.obtenerTodosLosAnunciosProgramados();
    }

    // Obtener un anuncio programado por su ID
    @GetMapping("/{id}")
    public Optional<AnuncioProgramado> obtenerAnuncioProgramadoPorId(@PathVariable Integer id) {
        return anuncioProgramadoService.obtenerAnuncioProgramadoPorId(id);
    }

    // Crear un nuevo anuncio programado
    @PostMapping
    public AnuncioProgramado crearAnuncioProgramado(@RequestBody AnuncioProgramado anuncioProgramado) {
        return anuncioProgramadoService.guardarAnuncioProgramado(anuncioProgramado);
    }

    // Actualizar un anuncio programado existente
    @PutMapping("/{id}")
    public AnuncioProgramado actualizarAnuncioProgramado(@PathVariable Integer id, @RequestBody AnuncioProgramado anuncioProgramado) {
        anuncioProgramado.setIdMensajeProgramado(id);
        return anuncioProgramadoService.guardarAnuncioProgramado(anuncioProgramado);
    }

    // Eliminar un anuncio programado por su ID
    @DeleteMapping("/{id}")
    public void eliminarAnuncioProgramado(@PathVariable Integer id) {
        anuncioProgramadoService.eliminarAnuncioProgramado(id);
    }
}
