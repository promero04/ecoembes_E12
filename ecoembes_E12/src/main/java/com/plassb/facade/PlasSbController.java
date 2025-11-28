package com.plassb.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plassb.entity.Asignacion;
import com.plassb.entity.Capacidad;
import com.plassb.entity.ContenedorAsignado;
import com.plassb.service.PlasSbService;

@RestController
@RequestMapping("/api/v1")
public class PlasSbController {

    private final PlasSbService plasSbService;

    public PlasSbController(PlasSbService plasSbService) {
        this.plasSbService = plasSbService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @PostMapping("/capacidades")
    public ResponseEntity<Capacidad> crearCapacidad(@RequestBody CapacidadRequest request) {
        Capacidad capacidad = plasSbService.upsertCapacidad(request.fecha(), request.capacidadTon());
        return ResponseEntity.status(HttpStatus.CREATED).body(capacidad);
    }

    @GetMapping("/capacidades")
    public List<Capacidad> capacidades(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return plasSbService.capacidades(fechaInicio, fechaFin);
    }

    @GetMapping("/capacidades/{fecha}")
    public ResponseEntity<Capacidad> capacidadPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return plasSbService.capacidad(fecha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/asignaciones")
    public ResponseEntity<AsignacionResponse> asignacion(@RequestBody AsignacionRequest request) {
        List<ContenedorAsignado> contenedores = request.contenedores().stream()
                .map(c -> new ContenedorAsignado(c.id(), c.numEnvases(), c.estado()))
                .collect(Collectors.toList());
        Asignacion asignacion = plasSbService.crearAsignacion(request.asignacionId(), request.fecha(),
                request.solicitante(), request.totalEnvases(), contenedores);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AsignacionResponse(asignacion.getAsignacionId(), asignacion.getEstado(),
                        "Asignacion registrada"));
    }

    @GetMapping("/asignaciones")
    public List<Asignacion> listarAsignaciones(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return plasSbService.asignaciones(fecha);
    }

    @GetMapping("/asignaciones/{asignacionId}")
    public ResponseEntity<Asignacion> asignacionDetalle(@PathVariable String asignacionId) {
        return plasSbService.asignacionPorId(asignacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/asignaciones/{asignacionId}/estado")
    public ResponseEntity<Asignacion> actualizarEstado(@PathVariable String asignacionId,
            @RequestBody EstadoRequest request) {
        return plasSbService.actualizarEstado(asignacionId, request.estado(), request.detalle())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alertas/saturacion")
    public ResponseEntity<Map<String, String>> alertaSaturacion(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("estado", "RECIBIDA"));
    }

    public record CapacidadRequest(LocalDate fecha, double capacidadTon) {
    }

    public record ContenedorRequest(Integer id, Integer numEnvases, String estado) {
    }

    public record AsignacionRequest(String asignacionId, LocalDate fecha, String solicitante, double totalEnvases,
            List<ContenedorRequest> contenedores) {
    }

    public record AsignacionResponse(String asignacionId, String estado, String mensaje) {
    }

    public record EstadoRequest(String estado, String detalle) {
    }
}
