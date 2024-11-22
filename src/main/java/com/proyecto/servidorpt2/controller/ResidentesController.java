
package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.Utils.ApiResponse;
import com.proyecto.servidorpt2.dto.DomicilioDTO;
import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.DomiciliosService;
import com.proyecto.servidorpt2.service.ResidentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/residentes")
public class ResidentesController {

    @Autowired
    private ResidentesService residentesService;
    @Autowired
    DomiciliosService domiciliosService;

    // Obtener todos los residentes
    @GetMapping("/obtenerResidenteDomicilio")
    public ResponseEntity<Object> obtenerTodosLosResidentesConDomicilio() {
        try {
            // Obtener todos los residentes con domicilio
            List<Residentes> residentes = residentesService.obtenerResidentesConDomicilio();

            // Obtener todos los domicilios desencriptados
            List<Domicilios> domicilios = domiciliosService.obtenerTodosLosDomicilios();

            // Crear un Map para relacionar idDomicilio con el objeto Domicilios
            Map<Integer, Domicilios> domicilioMap = domicilios.stream()
                    .collect(Collectors.toMap(Domicilios::getIdDomicilio, domicilio -> domicilio));

            // Asignar el domicilio correcto a cada residente
            List<Residentes> residentesConDomicilio = residentes.stream()
                    .filter(residente -> residente.getDomicilio() != null)
                    .peek(residente -> {
                        Domicilios domicilio = domicilioMap.get(residente.getDomicilio().getIdDomicilio());
                        if (domicilio != null) {
                            residente.setDomicilio(domicilio);
                        }
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(residentesConDomicilio, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener la lista de residentes con domicilio: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/obtenerTodosResidentes")
    public List<ResidenteDTO> obtenerTodosLosResidentesDTO() {
        // Obtener todos los residentes
        List<Residentes> residentes = residentesService.obtenerTodosLosResidentes();

        // Obtener todos los domicilios desencriptados
        List<Domicilios> domicilios = domiciliosService.obtenerTodosLosDomicilios();

        // Crear un Map para relacionar idDomicilio con el objeto Domicilios
        Map<Integer, Domicilios> domicilioMap = domicilios.stream()
                .collect(Collectors.toMap(Domicilios::getIdDomicilio, domicilio -> domicilio));

        // Mapear cada residente a su DTO, asignando el domicilio correcto
        return residentes.stream().map(residente -> {
            ResidenteDTO dto = new ResidenteDTO();
            dto.setIdResidente(residente.getIdResidente());
            dto.setNombre(residente.getNombre());
            dto.setApellido(residente.getApellido());
            dto.setApodo(residente.getApodo());
            dto.setComercio(residente.getComercio());

            // Asignar el domicilio correcto si existe
            if (residente.getDomicilio() != null) {
                Domicilios domicilio = domicilioMap.get(residente.getDomicilio().getIdDomicilio());
                if (domicilio != null) {
                    DomicilioDTO domicilioDTO = new DomicilioDTO();
                    domicilioDTO.setIdDomicilio(domicilio.getIdDomicilio());
                    domicilioDTO.setDireccion(domicilio.getDireccion());
                    domicilioDTO.setReferencia(domicilio.getReferencia());
                    domicilioDTO.setCoordenadas(domicilio.getCoordenadas());
                    dto.setDomicilio(domicilioDTO);
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }
    // Obtener un residente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerResidentePorId(@PathVariable Integer id) {
        try {
            Optional<Residentes> residente = residentesService.obtenerResidentePorId(id);
            if (residente.isPresent()) {
                return new ResponseEntity<>(residente.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al obtener el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un nuevo residente

    @PostMapping("/crearResidente")
    public ResponseEntity<ApiResponse> crearResidente(@RequestBody Residentes residente) {
        try {
            // Verificar si el residente trae un domicilio
            if (residente.getDomicilio() != null) {
                Domicilios domicilio = residente.getDomicilio();
                if (domicilio.getDireccion() != null &&!domicilio.getDireccion().isEmpty()) {
                    // Guardar el domicilio primero

                    domicilio = domiciliosService.guardarDomicilio(domicilio);
                    residente.setDomicilio(domicilio);
                } else {
                    return new ResponseEntity<>(new ApiResponse("error", "El domicilio proporcionado está incompleto"), HttpStatus.BAD_REQUEST);
                }
            }
            System.out.println("Residente: " + residente);
            // Guardar el residente
            residentesService.guardarResidente(residente);
            return new ResponseEntity<>(new ApiResponse("success", "Residente creado con éxito"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al crear el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(new ApiResponse("error", "Error al actualizar el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un residente por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarResidente(@PathVariable Integer id) {
        try {
            Optional<Residentes> residenteExistente = residentesService.obtenerResidentePorId(id);
            if (residenteExistente.isPresent()) {
                residentesService.eliminarResidente(id);
                return new ResponseEntity<>(new ApiResponse("success", "Residente eliminado con éxito"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
