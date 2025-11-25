package com.ecoembes.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ecoembes.entity.Personal;

@Service
public class AuthService {

    private final Map<String, Personal> usuarios = new ConcurrentHashMap<>();
    private final Map<String, Personal> tokensActivos = new ConcurrentHashMap<>();

    public AuthService() {
        // Usuarios de ejemplo para pruebas iniciales.
        registrarUsuarioDemo("Ana", "ana@ecoembes.com", "1234");
        registrarUsuarioDemo("Luis", "luis@ecoembes.com", "1234");
    }

    private void registrarUsuarioDemo(String nombre, String correo, String contrasena) {
        usuarios.put(correo, new Personal(nombre, correo, contrasena));
    }

    public Optional<String> login(String correo, String contrasena) {
        Personal personal = usuarios.get(correo);
        if (personal == null || !personal.getContrasena().equals(contrasena)) {
            return Optional.empty();
        }
        String token = UUID.randomUUID() + "-" + LocalDateTime.now();
        tokensActivos.put(token, personal);
        personal.setToken();
        return Optional.of(token);
    }

    public boolean logout(String token) {
        Personal personal = tokensActivos.remove(token);
        if (personal != null) {
            personal.setTokenNull();
            return true;
        }
        return false;
    }

    public Optional<Personal> validarToken(String token) {
        return Optional.ofNullable(tokensActivos.get(token));
    }
}
