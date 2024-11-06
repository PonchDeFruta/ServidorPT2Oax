package com.proyecto.servidorpt2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.dto.AnuncioDTO;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.format.DateTimeFormatter;

@Service
public class BroadcastService {

    public void enviarAnuncioPorBroadcast(Anuncio anuncio) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            int port = 9876;

            // Convertir LocalDateTime a String en el objeto DTO para la serialización
            AnuncioDTO anuncioDTO = new AnuncioDTO();
            anuncioDTO.setIdMensaje(anuncio.getIdMensaje());
            anuncioDTO.setTitulo(anuncio.getTitulo());
            anuncioDTO.setContenidoDelMensaje(anuncio.getContenidoDelMensaje());
            anuncioDTO.setFechaMensaje(anuncio.getFechaMensaje().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            anuncioDTO.setEsAudio(anuncio.isEsAudio());
            if (anuncio.getResidente() != null) {
                anuncioDTO.setIdResidente(anuncio.getResidente().getIdResidente());
            }

            // Configurar ObjectMapper para serializar el objeto DTO
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Serializar el objeto AnuncioDTO a JSON
            String mensajeJson = objectMapper.writeValueAsString(anuncioDTO);

            byte[] buffer = mensajeJson.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);
            socket.send(packet);
        } catch (Exception e) {
            System.err.println("Error al enviar anuncio por broadcast: " + e.getMessage());
        }
    }
}
