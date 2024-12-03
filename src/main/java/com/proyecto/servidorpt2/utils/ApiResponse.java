package com.proyecto.servidorpt2.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ApiResponse {
    private String status;
    private String message;
    private Map<String, Object> data;

    // Constructor for status and message only
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = new HashMap<>();
    }

        public void agregarID(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
    }


}
