package com.proyecto.servidorpt2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Registrar el módulo para manejar fechas en el formato de Java 8 (JavaTime)
        mapper.registerModule(new JavaTimeModule());

        // Deshabilitar la escritura de fechas como timestamps, para que se utilice el formato ISO por defecto
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Configuración adicional según tus necesidades
        // Por ejemplo: mapper.enable(SerializationFeature.INDENT_OUTPUT); para JSON indentado

        return mapper;
    }
}
