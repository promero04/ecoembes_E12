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
import com.ecoembes.entity.EstadoEnvase;
import com.ecoembes.service.ContenedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contenedores")
@Tag(name = "Contenedores", description = "Gestion y consulta de contenedores")
public class ContenedorController {

    private final ContenedorService contenedorService;

    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }

    @GetMapping
    @Operation(summary = "Listado de contenedores")
    public List<ContenedorDTO> listar(@RequestParam(value = "zona", required = false) String zona) {
        if (zona != null && !zona.isBlank()) {
            return contenedorService.buscarPorZona(zona);
        }
        return contenedorService.listar().stream().toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de contenedor")
    public ResponseEntity<ContenedorDTO> obtener(@PathVariable int id) {
        return contenedorService.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo contenedor")
    public ResponseEntity<ContenedorDTO> crear(@RequestBody ContenedorDTO dto) {
        ContenedorDTO creado = contenedorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}/sensor")
    @Operation(summary = "Actualizar lectura del sensor")
    public ResponseEntity<ContenedorDTO> actualizarSensor(@PathVariable int id,
            @RequestParam int numEnvases,
            @RequestParam EstadoEnvase estado) {
        Optional<ContenedorDTO> actualizado = contenedorService.actualizarSensor(id, numEnvases, estado);
        return actualizado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/uso")
    @Operation(summary = "Consulta de uso/estado por rango de fechas")
    public ResponseEntity<List<ContenedorDTO>> consultarUso(@PathVariable int id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<ContenedorDTO> lecturas = contenedorService.consultarUso(id, inicio, fin);
        if (lecturas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lecturas);
    }
}
