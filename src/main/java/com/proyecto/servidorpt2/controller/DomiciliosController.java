package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.service.DomiciliosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/domicilios")
public class DomiciliosController {

    @Autowired
    private DomiciliosService domiciliosService;

    // Obtener todos los domicilios
    @GetMapping
    public List<Domicilios> obtenerTodosLosDomicilios() {
        return domiciliosService.obtenerTodosLosDomicilios();
    }

    // Obtener un domicilio por su ID
    @GetMapping("/{id}")
    public Optional<Domicilios> obtenerDomicilioPorId(@PathVariable Integer id) {
        return domiciliosService.obtenerDomicilioPorId(id);
    }

    // Crear un nuevo domicilio
    @PostMapping
    public Domicilios crearDomicilio(@RequestBody Domicilios domicilio) {
        return domiciliosService.guardarDomicilio(domicilio);
    }

    // Actualizar un domicilio existente
    @PutMapping("/{id}")
    public Domicilios actualizarDomicilio(@PathVariable Integer id, @RequestBody Domicilios domicilio) {
        domicilio.setIdDomicilio(id);
        return domiciliosService.guardarDomicilio(domicilio);
    }

    // Eliminar un domicilio por su ID
    @DeleteMapping("/{id}")
    public void eliminarDomicilio(@PathVariable Integer id) {
        domiciliosService.eliminarDomicilio(id);
    }
}
