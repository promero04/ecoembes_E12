package com.ecoembes.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ecoembes.entity.Personal;
import com.ecoembes.repository.PersonalRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {

    private final PersonalRepository personalRepository;
    private final Map<String, Personal> tokensActivos = new ConcurrentHashMap<>();

    public AuthService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    @PostConstruct
    public void initDemoUsers() {
        registrarUsuarioDemo("Ana", "ana@ecoembes.com", "1234");
        registrarUsuarioDemo("Luis", "luis@ecoembes.com", "1234");
    }

    private void registrarUsuarioDemo(String nombre, String correo, String contrasena) {
        personalRepository.findByCorreo(correo)
                .orElseGet(() -> personalRepository.save(new Personal(nombre, correo, contrasena)));
    }

    public Optional<String> login(String correo, String contrasena) {
        Optional<Personal> personalOpt = personalRepository.findByCorreo(correo);
        if (personalOpt.isEmpty() || !personalOpt.get().getContrasena().equals(contrasena)) {
            return Optional.empty();
        }
        Personal personal = personalOpt.get();
        String token = UUID.randomUUID() + "-" + LocalDateTime.now();
        tokensActivos.put(token, personal);
        personal.setToken();
        personalRepository.save(personal);
        return Optional.of(token);
    }

    public boolean logout(String token) {
        Personal personal = tokensActivos.remove(token);
        if (personal != null) {
            personal.setTokenNull();
            personalRepository.save(personal);
            return true;
        }
        return false;
    }

    public Optional<Personal> validarToken(String token) {
        return Optional.ofNullable(tokensActivos.get(token));
    }
}
