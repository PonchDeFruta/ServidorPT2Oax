package com.proyecto.servidorpt2.service;

import com.proyecto.servidorpt2.dto.AnuncioDTO;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    private static final int PORT = 9876;
    private static final String BROADCAST_IP = "255.255.255.255";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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

    public static AnuncioDTO convertirAnuncioAAnuncioDTO(Anuncio anuncio) {
        AnuncioDTO dto = new AnuncioDTO();
        dto.setIdMensaje(anuncio.getIdMensaje());
        dto.setTitulo(anuncio.getTitulo());
        dto.setContenidoDelMensaje(anuncio.getContenidoDelMensaje());
        dto.setFechaMensaje(anuncio.getFechaMensaje() != null ? anuncio.getFechaMensaje().format(formatter) : null);
        dto.setEsAudio(anuncio.isEsAudio());
        dto.setIdResidente(anuncio.getResidente() != null ? anuncio.getResidente().getIdResidente() : null);
        return dto;
    }

    public static Anuncio convertirAnuncioDTOAAnuncio(AnuncioDTO anuncioDTO) {
        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(anuncioDTO.getTitulo());
        anuncio.setContenidoDelMensaje(anuncioDTO.getContenidoDelMensaje());
        anuncio.setFechaMensaje(anuncioDTO.getFechaMensaje() != null ? LocalDateTime.parse(anuncioDTO.getFechaMensaje(), formatter) : LocalDateTime.now());
        anuncio.setEsAudio(anuncioDTO.isEsAudio());
        return anuncio;
    }

    public void eliminarTodosLosAnuncios() {
        anuncioRepository.deleteAll();
    }

}
