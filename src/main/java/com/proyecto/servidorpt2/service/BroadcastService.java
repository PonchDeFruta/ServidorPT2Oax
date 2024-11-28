package com.proyecto.servidorpt2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.servidorpt2.dto.AnuncioDTO;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BroadcastService {

    private static final Logger logger = LoggerFactory.getLogger(BroadcastService.class);

    private static final int PORT = 9876;
    private static final String BROADCAST_IP = "192.168.3.255"; // Ajustar a la ip

    public void enviarAnuncioPorBroadcast(AnuncioDTO anuncioDTO) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            ObjectMapper mapper = new ObjectMapper();
            String mensaje = mapper.writeValueAsString(anuncioDTO);

            byte[] buffer = mensaje.getBytes();
            InetAddress address = InetAddress.getByName(BROADCAST_IP);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);

            logger.info("Broadcasting to {}:{} - Message: {}", BROADCAST_IP, PORT, mensaje);
            socket.send(packet);
            logger.info("Message successfully sent.");
        } catch (Exception e) {
            logger.error("Error while broadcasting message", e);
        }
    }
}
