package com.proyecto.servidorpt2.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class Encriptar {

    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    // Constructor que recibe la clave de encriptación
    public Encriptar(@Value("${encryption.secret-key}") String key) throws Exception {
        // Validar que la longitud de la clave sea válida (16, 24, 32 bytes para AES)
        if (key.length() != 16 && key.length() != 24 && key.length() != 32) {
            throw new IllegalArgumentException("La longitud de la clave debe ser de 16, 24 o 32 bytes");
        }

        // Crear el objeto SecretKeySpec con la clave recibida
        this.secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

        // Obtener la instancia del Cipher para AES
        this.cipher = Cipher.getInstance("AES");
    }

    // Método para encriptar datos
    public String encrypt(String data) throws Exception {
        try {
            // Inicializar el Cipher en modo de encriptación
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            // Encriptar los datos y devolver el resultado en Base64
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar los datos", e);
        }
    }

    // Método para desencriptar datos
    public String decrypt(String encryptedData) throws Exception {
        try {
            // Inicializar el Cipher en modo de desencriptación
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // Decodificar los datos en Base64 y desencriptarlos
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decodedData));
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar los datos", e);
        }
    }
}
