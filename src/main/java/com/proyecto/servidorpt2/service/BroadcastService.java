package com.proyecto.servidorpt2.service;

import com.google.gson.Gson;
import com.proyecto.servidorpt2.entities.Anuncio;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Service
public class BroadcastService {

    public void enviarAnuncioPorBroadcast(Anuncio anuncio) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            int port = 9876;

            Gson gson = new Gson();
            String mensajeJson = gson.toJson(anuncio);

            byte[] buffer = mensajeJson.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);
            socket.send(packet);
        } catch (Exception e) {
            System.err.println("Error al enviar anuncio por broadcast: " + e.getMessage());
        }
    }
}