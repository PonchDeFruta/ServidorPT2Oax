package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.Utils.Encriptar;
import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.dto.DomicilioDTO;
import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.entities.Dispositivo;
import com.proyecto.servidorpt2.entities.Domicilios;
import com.proyecto.servidorpt2.entities.Residentes;
import com.proyecto.servidorpt2.repository.AnuncioRepository;
import com.proyecto.servidorpt2.repository.DispositivoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnuncioService {
    private static final Logger logger = LoggerFactory.getLogger(BroadcastService.class);

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private Encriptar encryptionService;

    public Optional<AnuncioDTO> obtenerAnuncioDescifradoPorId(Integer id) {
        return anuncioRepository.findById(id)
                .map(this::convertirAnuncioADTOConDescifrado);
    }

    public List<AnuncioDTO> obtenerTodosLosAnunciosDescifrados() {
        return anuncioRepository.findAll().stream()
                .map(this::convertirAnuncioADTOConDescifrado)
                .collect(Collectors.toList());
    }

    public AnuncioDTO guardarAnuncioConCifrado(AnuncioDTO anuncioDTO) {
        Anuncio anuncio = convertirDTOAAnuncioConCifrado(anuncioDTO);
        Anuncio anuncioGuardado = anuncioRepository.save(anuncio);
        return convertirAnuncioADTOConDescifrado(anuncioGuardado);
    }

    public void eliminarAnuncio(Integer id) {
        anuncioRepository.deleteById(id);
    }

    public void eliminarTodosLosAnuncios() {
        anuncioRepository.deleteAll();
    }

    private AnuncioDTO convertirAnuncioADTOConDescifrado(Anuncio anuncio) {
        AnuncioDTO dto = new AnuncioDTO();
        dto.setIdMensaje(anuncio.getIdMensaje());
        dto.setTitulo(anuncio.getTitulo());
        dto.setContenidoDelMensaje(anuncio.getContenidoDelMensaje());
        dto.setFechaMensaje(anuncio.getFechaMensaje().toString());
        dto.setEsAudio(anuncio.isEsAudio());

        if (anuncio.getResidente() != null) {
            try {
                dto.setResidente(convertirResidenteADTOConDescifrado(anuncio.getResidente()));
            } catch (Exception e) {
                System.err.println("Error al descifrar los datos del residente: " + e.getMessage());
            }
        }
        return dto;
    }

    private ResidenteDTO convertirResidenteADTOConDescifrado(Residentes residente) {
        ResidenteDTO residenteDTO = new ResidenteDTO();
        residenteDTO.setIdResidente(residente.getIdResidente());

        // Descifra solo los campos sensibles
        residenteDTO.setNombre(safeDecrypt(residente.getNombre()));
        residenteDTO.setApellido(safeDecrypt(residente.getApellido()));

        // Asigna directamente los campos que no necesitan cifrado
        residenteDTO.setApodo(residente.getApodo());
        residenteDTO.setComercio(residente.getComercio());

        if (residente.getDomicilio() != null) {
            try {
                residenteDTO.setDomicilio(convertirDomicilioADTOConDescifrado(residente.getDomicilio()));
            } catch (Exception e) {
                System.err.println("Error al descifrar los datos del domicilio: " + e.getMessage());
            }
        }
        return residenteDTO;
    }

    private DomicilioDTO convertirDomicilioADTOConDescifrado(Domicilios domicilio) {
        DomicilioDTO domicilioDTO = new DomicilioDTO();
        domicilioDTO.setIdDomicilio(domicilio.getIdDomicilio());

        domicilioDTO.setDireccion(safeDecrypt(domicilio.getDireccion()));
        domicilioDTO.setReferencia(safeDecrypt(domicilio.getReferencia()));
        domicilioDTO.setCoordenadas(safeDecrypt(domicilio.getCoordenadas()));

        return domicilioDTO;
    }

    private Anuncio convertirDTOAAnuncioConCifrado(AnuncioDTO anuncioDTO) {
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(anuncioDTO.getTitulo());
        anuncio.setContenidoDelMensaje(anuncioDTO.getContenidoDelMensaje());
        anuncio.setFechaMensaje(LocalDateTime.parse(anuncioDTO.getFechaMensaje()));
        anuncio.setEsAudio(anuncioDTO.isEsAudio());

        if (anuncioDTO.getIdResidente() != null) {
            Residentes residente = new Residentes();
            residente.setIdResidente(anuncioDTO.getIdResidente());
            anuncio.setResidente(residente);
        }
        return anuncio;
    }

    private Residentes convertirDTOAResidenteConCifrado(ResidenteDTO residenteDTO) {
        Residentes residente = new Residentes();
        residente.setIdResidente(residenteDTO.getIdResidente());

        // Cifra solo los campos sensibles
        residente.setNombre(safeEncrypt(residenteDTO.getNombre()));
        residente.setApellido(safeEncrypt(residenteDTO.getApellido()));

        // Almacena directamente los campos que no requieren cifrado
        residente.setApodo(residenteDTO.getApodo());
        residente.setComercio(residenteDTO.getComercio());

        if (residenteDTO.getDomicilio() != null) {
            residente.setDomicilio(convertirDTOADomicilioConCifrado(residenteDTO.getDomicilio()));
        }
        return residente;
    }

    private Domicilios convertirDTOADomicilioConCifrado(DomicilioDTO domicilioDTO) {
        Domicilios domicilio = new Domicilios();
        domicilio.setIdDomicilio(domicilioDTO.getIdDomicilio());

        domicilio.setDireccion(safeEncrypt(domicilioDTO.getDireccion()));
        domicilio.setReferencia(safeEncrypt(domicilioDTO.getReferencia()));
        domicilio.setCoordenadas(safeEncrypt(domicilioDTO.getCoordenadas()));

        return domicilio;
    }

    private String safeDecrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return null;
        }
        try {
            return encryptionService.decrypt(encryptedData);
        } catch (Exception e) {
            System.err.println("Error al descifrar el dato: " + e.getMessage());
            return null;
        }
    }

    private String safeEncrypt(String data) {
        if (data == null) {
            return null;
        }
        try {
            return encryptionService.encrypt(data);
        } catch (Exception e) {
            System.err.println("Error al cifrar el dato: " + e.getMessage());
            return null;
        }
    }
}
