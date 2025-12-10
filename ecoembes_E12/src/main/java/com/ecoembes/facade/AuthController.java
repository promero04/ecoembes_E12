package com.ecoembes.facade;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoembes.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Login/logout de personal")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login con correo y contraseña")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String contrasena = body.get("contrasena");
        Optional<String> token = authService.login(correo, contrasena);
        return token.map(t -> ResponseEntity.ok(Map.of("token", t)))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Credenciales invalidas")));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("X-Auth-Token") String token) {
        boolean ok = authService.logout(token);
        if (ok) {
            return ResponseEntity.ok(Map.of("status", "logout"));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Token no valido"));
    }
}
