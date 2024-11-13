package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.dto.DomicilioDTO;
import com.proyecto.servidorpt2.dto.ResidenteDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    private static final int PORT = 9876;
    private static final String BROADCAST_IP = "255.255.255.255";


    public List<Anuncio> obtenerTodosLosAnuncios() {
        return anuncioRepository.findAll();
    }


    public Optional<Anuncio> obtenerAnuncioPorId(Integer id) {
        return anuncioRepository.findById(id);
    }

    public Anuncio guardarAnuncio(Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }


    public void eliminarAnuncio(Integer id) {
        anuncioRepository.deleteById(id);
    }


    public void eliminarTodosLosAnuncios() {
        anuncioRepository.deleteAll();
    }

    public AnuncioDTO convertirAnuncioAAnuncioDTO(Anuncio anuncio) {
        AnuncioDTO dto = new AnuncioDTO();
        dto.setIdMensaje(anuncio.getIdMensaje());
        dto.setTitulo(anuncio.getTitulo());
        dto.setContenidoDelMensaje(anuncio.getContenidoDelMensaje());
        dto.setFechaMensaje(anuncio.getFechaMensaje().toString());
        dto.setEsAudio(anuncio.isEsAudio());

        if (anuncio.getResidente() != null) {
            ResidenteDTO residenteDTO = new ResidenteDTO();
            residenteDTO.setIdResidente(anuncio.getResidente().getIdResidente());
            residenteDTO.setNombre(anuncio.getResidente().getNombre());
            residenteDTO.setApellido(anuncio.getResidente().getApellido());
            residenteDTO.setApodo(anuncio.getResidente().getApodo());
            residenteDTO.setComercio(anuncio.getResidente().getComercio());

            if (anuncio.getResidente().getDomicilio() != null) {
                DomicilioDTO domicilioDTO = new DomicilioDTO();
                domicilioDTO.setIdDomicilio(anuncio.getResidente().getDomicilio().getIdDomicilio());
                domicilioDTO.setDireccion(anuncio.getResidente().getDomicilio().getDireccion());
                domicilioDTO.setReferencia(anuncio.getResidente().getDomicilio().getReferencia());
                domicilioDTO.setCoordenadas(anuncio.getResidente().getDomicilio().getCoordenadas());
                residenteDTO.setDomicilio(domicilioDTO);
            }

            dto.setResidente(residenteDTO);
        }

        return dto;
    }




}
