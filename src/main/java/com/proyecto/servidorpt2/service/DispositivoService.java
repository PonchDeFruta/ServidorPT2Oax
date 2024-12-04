package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.dto.DispositivoDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.entities.Dispositivo;
import com.proyecto.servidorpt2.repository.DispositivoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DispositivoService {
    private static final Logger logger = LoggerFactory.getLogger(DispositivoService.class);

    @Autowired
    private DispositivoRepository dispositivoRepository;

    // Obtener todos los dispositivos


    // Obtener un dispositivo por su ID
    public Optional<Dispositivo> obtenerDispositivoPorId(Integer id) {
        return dispositivoRepository.findById(id);
    }

    // Crear o actualizar un dispositivo
    public Dispositivo guardarDispositivo(Dispositivo dispositivo) {
        return dispositivoRepository.save(dispositivo);
    }

    // Eliminar un dispositivo por su ID
    public void eliminarDispositivo(Integer id) {
        dispositivoRepository.deleteById(id);
    }

    public List<DispositivoDTO> obtenerTodosLosDispositivos() {
        // Recuperamos todos los dispositivos desde la base de datos
        List<Dispositivo> dispositivos = dispositivoRepository.findAll();

        // Mapeamos la lista de dispositivos a una lista de DTOs
        return dispositivos.stream().map(dispositivo -> {
            return new DispositivoDTO(
                    dispositivo.getIdDispositivo(),
                    dispositivo.getNombreDispositivo(),
                    dispositivo.getContadorRecepcionMensajes(),
                    dispositivo.getAnuncio() != null ? dispositivo.getAnuncio().getIdMensaje() : null
            );
        }).collect(Collectors.toList());
    }

    public void registrarRecepcionUnica(Integer idMensaje, String nombreDispositivo) {
        if (idMensaje == null || nombreDispositivo == null) return;

        // Buscar si ya existe un registro para este dispositivo y mensaje
        dispositivoRepository.findByNombreDispositivoAndAnuncio_IdMensaje(nombreDispositivo, idMensaje)
                .ifPresentOrElse(
                        dispositivo -> {
                            logger.info("Mensaje {} ya recibido por el dispositivo {}", idMensaje, nombreDispositivo);
                        },
                        () -> {
                            // Crear un nuevo registro si no existe
                            Dispositivo nuevoDispositivo = new Dispositivo();
                            nuevoDispositivo.setNombreDispositivo(nombreDispositivo);
                            nuevoDispositivo.setContadorRecepcionMensajes(1);

                            // Asociar el mensaje (Anuncio)
                            Anuncio anuncio = new Anuncio();
                            anuncio.setIdMensaje(idMensaje); // Solo necesita el ID para la asociación
                            nuevoDispositivo.setAnuncio(anuncio);

                            // Guardar en la base de datos
                            dispositivoRepository.save(nuevoDispositivo);
                            logger.info("Mensaje {} registrado para el dispositivo {}", idMensaje, nombreDispositivo);
                        }
                );
    }
}
