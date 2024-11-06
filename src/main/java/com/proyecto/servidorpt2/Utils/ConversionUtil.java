package com.proyecto.servidorpt2.Utils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime parseStringToLocalDateTime(String fechaString) {
        return LocalDateTime.parse(fechaString, formatter);
    }

    public static String formatLocalDateTimeToString(LocalDateTime fecha) {
        return fecha.format(formatter);
    }
}

