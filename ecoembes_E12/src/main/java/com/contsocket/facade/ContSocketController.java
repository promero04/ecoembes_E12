package com.contsocket.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contsocket.model.AsignacionSocket;
import com.contsocket.model.CapacidadSocket;
import com.contsocket.model.ContenedorSocket;
import com.contsocket.service.ContSocketService;

@RestController
@RequestMapping("/api/v1")
public class ContSocketController {

    private final ContSocketService contSocketService;

    public ContSocketController(ContSocketService contSocketService) {
        this.contSocketService = contSocketService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @PutMapping("/capacidades/{fecha}")
    public ResponseEntity<CapacidadSocket> capacidad(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestBody CapacidadRequest request) {
        CapacidadSocket cap = contSocketService.upsertCapacidad(fecha, request.capacidadTon());
        return ResponseEntity.ok(cap);
    }

    @GetMapping("/capacidades/{fecha}")
    public ResponseEntity<CapacidadSocket> capacidad(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return contSocketService.capacidad(fecha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/asignaciones")
    public ResponseEntity<AsignacionResponse> crearAsignacion(@RequestBody AsignacionRequest request) {
        List<ContenedorSocket> contenedores = request.contenedores();
        AsignacionSocket asig = contSocketService.crearAsignacion(request.asignacionId(), request.fecha(),
                request.solicitante(), request.totalEnvases(), contenedores);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AsignacionResponse(asig.getAsignacionId(), asig.getEstado(), "Asignacion registrada"));
    }

    @GetMapping("/asignaciones/{asignacionId}")
    public ResponseEntity<AsignacionSocket> detalle(@PathVariable String asignacionId) {
        return contSocketService.asignacion(asignacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/asignaciones/{asignacionId}/estado")
    public ResponseEntity<AsignacionSocket> estado(@PathVariable String asignacionId,
            @RequestBody EstadoRequest request) {
        return contSocketService.actualizarEstado(asignacionId, request.estado(), request.detalle())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public record CapacidadRequest(double capacidadTon) {
    }

    public record AsignacionRequest(String asignacionId, LocalDate fecha, String solicitante, double totalEnvases,
            List<ContenedorSocket> contenedores) {
    }

    public record AsignacionResponse(String asignacionId, String estado, String mensaje) {
    }

    public record EstadoRequest(String estado, String detalle) {
    }
}
