package com.proyecto.servidorpt2.service;

import com.google.gson.Gson;
import com.proyecto.servidorpt2.entities.Anuncio;
import com.proyecto.servidorpt2.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    // Método para enviar el anuncio como broadcast UDP
    public void enviarBroadcastUDP(Anuncio anuncio) {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                Gson gson = new Gson();
                String anuncioJson = gson.toJson(anuncio);  // Convertir a JSON
                byte[] buffer = anuncioJson.getBytes();

                InetAddress address = InetAddress.getByName(BROADCAST_IP);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
                socket.send(packet);  // Enviar el paquete
                System.out.println("Anuncio enviado por broadcast UDP: " + anuncioJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
