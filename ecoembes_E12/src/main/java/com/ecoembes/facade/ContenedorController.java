package com.ecoembes.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.ContenedorInfoDTO;
import com.ecoembes.entity.EstadoEnvase;
import com.ecoembes.service.ContenedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contenedor")
@Tag(name = "Contenedor", description = "Gestión y consulta de contenedores")
public class ContenedorController {

    private final ContenedorService contenedorService;

    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    @GetMapping
    @Operation(summary = "Listado de contenedores con filtro por zona y fecha")
    public List<ContenedorInfoDTO> listar(@RequestParam(value = "zona", required = false) String zona,
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha != null) {
            return contenedorService.buscarPorFechaYZona(fecha, zona).stream().map(this::toPublicDto).toList();
        }
        if (zona != null && !zona.isBlank()) {
            return contenedorService.buscarPorZona(zona).stream().map(this::toPublicDto).toList();
        }
        return contenedorService.listar().stream().map(this::toPublicDto).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de contenedor")
    public ResponseEntity<ContenedorInfoDTO> obtener(@PathVariable int id) {
        return contenedorService.obtener(id)
                .map(this::toPublicDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear contenedor")
    public ResponseEntity<ContenedorInfoDTO> crear(@RequestBody ContenedorDTO dto) {
        ContenedorDTO creado = contenedorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toPublicDto(creado));
    }

    @PutMapping("/{id}/sensor")
    @Operation(summary = "Actualizar lectura del sensor")
    public ResponseEntity<ContenedorInfoDTO> actualizarSensor(@PathVariable int id,
            @RequestParam int numEnvases,
            @RequestParam EstadoEnvase estado) {
        Optional<ContenedorDTO> actualizado = contenedorService.actualizarSensor(id, numEnvases, estado);
        return actualizado.map(this::toPublicDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/uso")
    @Operation(summary = "Consulta de uso por rango de fechas")
    public ResponseEntity<List<ContenedorInfoDTO>> consultarUso(@PathVariable int id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<ContenedorInfoDTO> lecturas = contenedorService.consultarUso(id, inicio, fin).stream()
                .map(this::toPublicDto)
                .toList();
        if (lecturas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lecturas);
    }

    private ContenedorInfoDTO toPublicDto(ContenedorDTO contenedor) {
        ContenedorInfoDTO dto = new ContenedorInfoDTO();
        dto.setUbicacion(contenedor.getUbicacion());
        dto.setCapInicial(contenedor.getCapInicial());
        dto.setEstadoEnvase(contenedor.getEstadoEnvase());
        dto.setNumEnvases(contenedor.getNumEnvases());
        return dto;
    }
}
