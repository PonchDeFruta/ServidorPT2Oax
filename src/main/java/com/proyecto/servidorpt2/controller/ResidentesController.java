
package com.proyecto.servidorpt2.controller;

import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.service.AnuncioService;
import com.proyecto.servidorpt2.utils.ApiResponse;
import com.proyecto.servidorpt2.dto.DomicilioDTO;
import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.service.DomiciliosService;
import com.proyecto.servidorpt2.service.ResidentesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ResidentesController.class);

    @Autowired
    private ResidentesService residentesService;
    @Autowired
    DomiciliosService domiciliosService;

    @Autowired
    AnuncioService anuncioService;

    @Autowired
    AnuncioController anuncioController;


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
        // Obtener todos los residentes, incluyendo el ID del domicilio
        List<Residentes> residentes = residentesService.obtenerTodosLosResidentes();

        // Mapear cada residente a su DTO
        return residentes.stream().map(residente -> {
            ResidenteDTO dto = new ResidenteDTO();
            dto.setIdResidente(residente.getIdResidente());
            dto.setNombre(residente.getNombre());
            dto.setApellido(residente.getApellido());
            dto.setApodo(residente.getApodo());
            dto.setComercio(residente.getComercio());

            // Asignar el domicilio solo si no es nulo
            if (residente.getDomicilio() != null) {
                DomicilioDTO domicilioDTO = new DomicilioDTO();
                domicilioDTO.setIdDomicilio(residente.getDomicilio().getIdDomicilio());
                // No se consultan más detalles del domicilio, solo su ID
                dto.setDomicilio(domicilioDTO);
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
            ApiResponse respuesta = new ApiResponse("success", "Residente creado con éxito");
            respuesta.agregarID("id", residente.getIdResidente());
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
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
    // Asociar un domicilio existente a un residente
    @PutMapping("/{id}/domicilio/{domicilioId}")
    public ResponseEntity<ApiResponse> asociarDomicilioAResidente(@PathVariable Integer id, @PathVariable Integer domicilioId) {
        try {
            // Verificar si el residente existe
            Optional<Residentes> residenteOpt = residentesService.obtenerResidentePorId(id);
            if (residenteOpt.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("error", "Residente no encontrado con ID: " + id), HttpStatus.NOT_FOUND);
            }

            // Verificar si el domicilio existe (sin descifrar)
            Optional<Domicilios> domicilioOpt = domiciliosService.obtenerDomicilioSinDescifrarPorId(domicilioId);
            if (domicilioOpt.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("error", "Domicilio no encontrado con ID: " + domicilioId), HttpStatus.NOT_FOUND);
            }

            // Asociar el domicilio al residente sin modificar el domicilio
            Residentes residente = residenteOpt.get();
            residente.setDomicilio(domicilioOpt.get());
            residentesService.guardarResidente(residente);

            // Responder con éxito
            ApiResponse response = new ApiResponse("success", "Domicilio asociado al residente con éxito");
            response.agregarID("idResidente", residente.getIdResidente());
            response.agregarID("idDomicilio", domicilioId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("error", "Error al asociar el domicilio al residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarResidenteYAsociados(@PathVariable Integer id) {
        try {
            // Validar que el ID no sea nulo
            if (id == null) {
                throw new IllegalArgumentException("El ID del residente no puede ser nulo");
            }

            // Obtener el residente por ID
            Optional<Residentes> residenteOpt = residentesService.obtenerResidentePorId(id);
            if (residenteOpt.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse("error", "Residente con ID " + id + " no encontrado"), HttpStatus.NOT_FOUND);
            }

            Residentes residente = residenteOpt.get();

            // Obtener el ID del domicilio asociado (si existe)
            Integer idDomicilio = (residente.getDomicilio() != null) ? residente.getDomicilio().getIdDomicilio() : null;

            // Actualizar anuncios asociados al residente
            List<Anuncio> anunciosAsociados = anuncioService.obtenerAnunciosPorIdResidente(id);
            if (!anunciosAsociados.isEmpty()) {
                anunciosAsociados.forEach(anuncio -> {
                    anuncio.setResidente(null); // Desasociar residente
                    anuncio.setTitulo(anuncio.getTitulo()); // Mantener título
                    anuncio.setContenidoDelMensaje(anuncio.getContenidoDelMensaje()); // Mantener contenido
                    anuncio.setFechaMensaje(anuncio.getFechaMensaje()); // Mantener fecha
                    anuncio.setEsAudio(anuncio.isEsAudio()); // Mantener si es audio
                    anuncioService.actualizarAnuncio(anuncio); // Actualizar anuncio en la base de datos
                });
                logger.info("Anuncios asociados al residente con ID {} han sido actualizados para eliminar la relación con el residente.", id);
            }

            // Eliminar el domicilio asociado (si existe)
            if (idDomicilio != null) {
                domiciliosService.eliminarDomicilio(idDomicilio);
                logger.info("Domicilio con ID {} eliminado.", idDomicilio);
            }

            // Eliminar el residente
            residentesService.eliminarResidente(id);
            logger.info("Residente con ID {} eliminado.", id);

            // Responder con éxito
            ApiResponse response = new ApiResponse("success", "Residente y sus asociaciones eliminados con éxito");
            response.agregarID("idResidente", id);
            response.agregarID("idDomicilio", idDomicilio);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            logger.error("Error de validación: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error al eliminar el residente con ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(new ApiResponse("error", "Error al eliminar el residente: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
