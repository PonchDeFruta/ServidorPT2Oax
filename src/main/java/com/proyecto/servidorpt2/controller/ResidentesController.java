package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.DomiciliosService;
import com.proyecto.servidorpt2.service.ResidentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/residentes")
public class ResidentesController {

    @Autowired
    private ResidentesService residentesService;

    @Autowired
    private DomiciliosService domiciliosService;

    // Obtener todos los residentes
    @GetMapping
    public ResponseEntity<ApiResponse> obtenerTodosLosResidentes() {
        try {
            List<Residentes> residentes = residentesService.obtenerTodosLosResidentes();
            return new ResponseEntity<>(new ApiResponse("success", "Residentes obtenidos con éxito"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener la lista de residentes"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un residente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtenerResidentePorId(@PathVariable Integer id) {
        try {
            Optional<Residentes> residente = residentesService.obtenerResidentePorId(id);
            if (residente.isPresent()) {
                return new ResponseEntity<>(new ApiResponse("success", "Residente encontrado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el residente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo residente
    @PostMapping
    public ResponseEntity<ApiResponse> crearResidente(@RequestBody Residentes residente) {
        try {
            residentesService.guardarResidente(residente);
            return new ResponseEntity<>(new ApiResponse("success", "Residente creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el residente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un residente existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarResidente(@PathVariable Integer id, @RequestBody Residentes residente) {
        try {
            Optional<Residentes> residenteExistente = residentesService.obtenerResidentePorId(id);
            if (residenteExistente.isPresent()) {
                residente.setIdResidente(id);
                residentesService.guardarResidente(residente);
                return new ResponseEntity<>(new ApiResponse("success", "Residente actualizado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el residente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un residente por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarResidente(@PathVariable Integer id) {
        try {
            Optional<Residentes> residenteExistente = residentesService.obtenerResidentePorId(id);
            if (residenteExistente.isPresent()) {
                residentesService.eliminarResidente(id);
                return new ResponseEntity<>(new ApiResponse("success", "Residente eliminado con éxito"), HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el residente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Registrar un residente con un domicilio existente o nuevo
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse> registrarResidente(@RequestBody ResidenteDTO residenteDTO) {
        try {
            Domicilios domicilio;

            // Si se proporciona un domicilioId, buscar el domicilio existente
            if (residenteDTO.getDomicilioId() != null) {
                Optional<Domicilios> domicilioExistente = domiciliosService.obtenerDomicilioPorId(residenteDTO.getDomicilioId());
                if (!domicilioExistente.isPresent()) {
                    return new ResponseEntity<>(new ApiResponse("error", "Domicilio no encontrado con ID: " + residenteDTO.getDomicilioId()), HttpStatus.BAD_REQUEST);
                }
                domicilio = domicilioExistente.get();
            } else {
                // Crear un nuevo domicilio si no se proporcionó un domicilioId
                Domicilios nuevoDomicilio = new Domicilios();
                nuevoDomicilio.setDireccion(residenteDTO.getDireccion());
                nuevoDomicilio.setReferencia(residenteDTO.getReferencia());
                nuevoDomicilio.setCoordenadas(residenteDTO.getCoordenadas());
                domicilio = domiciliosService.guardarDomicilio(nuevoDomicilio);
            }

            // Crear el nuevo residente y asociarlo al domicilio
            Residentes nuevoResidente = new Residentes();
            nuevoResidente.setNombre(residenteDTO.getNombre());
            nuevoResidente.setApellido(residenteDTO.getApellido());
            nuevoResidente.setApodo(residenteDTO.getApodo());
            nuevoResidente.setComercio(residenteDTO.getComercio());
            nuevoResidente.setDomicilio(domicilio);

            // Guardar el residente en la base de datos
            residentesService.guardarResidente(nuevoResidente);
            return new ResponseEntity<>(new ApiResponse("success", "Residente registrado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al registrar el residente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
