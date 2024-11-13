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
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BroadcastService {

    private static final int PORT = 9876;
    private static final String BROADCAST_IP = "255.255.255.255";

    public void enviarAnuncioPorBroadcast(AnuncioDTO anuncioDTO) {
        try (DatagramSocket socket = new DatagramSocket()) {
            ObjectMapper mapper = new ObjectMapper();
            String mensaje = mapper.writeValueAsString(anuncioDTO);  // Convertir a JSON

            byte[] buffer = mensaje.getBytes();
            InetAddress address = InetAddress.getByName(BROADCAST_IP);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


