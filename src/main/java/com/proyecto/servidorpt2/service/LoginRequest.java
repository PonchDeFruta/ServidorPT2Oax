package com.proyecto.servidorpt2.service;

import org.springframework.beans.factory.annotation.Value;

public class LoginRequest {
    @Value("${admin.master.password}")
    private String password;
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}