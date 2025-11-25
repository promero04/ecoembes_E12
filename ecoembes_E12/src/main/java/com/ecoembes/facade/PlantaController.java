package com.ecoembes.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecoembes.DTO.CapacidadPlantasDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;
import com.ecoembes.service.AuthService;
import com.ecoembes.service.ContenedorService;
import com.ecoembes.service.PlantaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/plantas")
@Tag(name = "Plantas", description = "Capacidades y asignación de contenedores a plantas de reciclaje")
public class PlantaController {

    private final PlantaService plantaService;
    private final ContenedorService contenedorService;
    private final AuthService authService;

    public PlantaController(PlantaService plantaService, ContenedorService contenedorService, AuthService authService) {
        this.plantaService = plantaService;
        this.contenedorService = contenedorService;
        this.authService = authService;
    }

    @GetMapping("/capacidades")
    @Operation(summary = "Capacidad disponible por planta para una fecha")
    public List<CapacidadPlantasDTO> capacidades(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return plantaService.listarCapacidades(fecha);
    }

    @GetMapping("/capacidad")
    @Operation(summary = "Capacidad de una planta en una fecha")
    public ResponseEntity<CapacidadPlantasDTO> capacidad(
            @RequestParam String planta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return plantaService.getCapacidad(planta, fecha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/asignar")
    @Operation(summary = "Asignar contenedores a una planta")
    public ResponseEntity<?> asignar(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody Map<String, Object> payload) {
        String planta = (String) payload.get("planta");
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) payload.getOrDefault("contenedores", new ArrayList<>());

        if (authService.validarToken(token).isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Token no válido"));
        }

        List<ContenedorDTO> contenedores = ids.stream()
                .map(contenedorService::obtener)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Optional<RegistroAuditoriaDTO> asignacion = plantaService.asignar(planta, contenedores,
                authService.validarToken(token).get().getNombre());
        return asignacion.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest()
                        .body(Map.of("error", "Capacidad insuficiente o planta no encontrada")));
    }
}
