package com.ecoembes.facade;

import java.time.LocalDate;
import java.util.List;
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

import com.ecoembes.DTO.AsignacionRequestDTO;
import com.ecoembes.DTO.CapacidadPlantaDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.ContenedorInfoDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;
import com.ecoembes.DTO.RegistroAuditoriaInfoDTO;
import com.ecoembes.entity.Personal;
import com.ecoembes.service.AuthService;
import com.ecoembes.service.ContenedorService;
import com.ecoembes.service.PlantaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/planta")
@Tag(name = "Planta", description = "Capacidades y asignación de contenedores")
public class PlantaController {

    private final PlantaService plantaService;
    private final ContenedorService contenedorService;
    private final AuthService authService;

    public PlantaController(PlantaService plantaService, ContenedorService contenedorService, AuthService authService) {
        this.plantaService = plantaService;
        this.contenedorService = contenedorService;
        this.authService = authService;
    }

    @GetMapping("/capacidad")
    @Operation(summary = "Consultar capacidades (todas o por planta y fecha)")
    public ResponseEntity<?> capacidad(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam(required = false) String planta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (authService.validarToken(token).isEmpty()) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Token no valido"));
        }
        if (planta != null && !planta.isBlank()) {
            return plantaService.getCapacidad(planta, fecha)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        List<CapacidadPlantaDTO> capacidades = plantaService.listarCapacidades(fecha);
        return ResponseEntity.ok(capacidades);
    }

    @PostMapping("/asignacion")
    @Operation(summary = "Registrar asignación de contenedores en una planta")
    public ResponseEntity<?> asignar(
            @RequestHeader("X-Auth-Token") String token,
            @RequestBody AsignacionRequestDTO payload) {
        Optional<Personal> personalOpt = authService.validarToken(token);
        if (personalOpt.isEmpty()) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", "Token no valido"));
        }
        String planta = payload.getPlanta();
        List<Integer> ids = payload.getContenedores();

        List<ContenedorDTO> contenedores = ids.stream()
                .map(contenedorService::obtener)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Optional<RegistroAuditoriaDTO> asignacion = plantaService.asignar(planta, contenedores, personalOpt.get());
        return asignacion.<ResponseEntity<?>>map(a -> ResponseEntity.ok(toAuditoriaInfo(a)))
                .orElse(ResponseEntity.badRequest()
                        .body(java.util.Map.of("error", "Capacidad insuficiente o planta no encontrada")));
    }

    private RegistroAuditoriaInfoDTO toAuditoriaInfo(RegistroAuditoriaDTO dto) {
        RegistroAuditoriaInfoDTO info = new RegistroAuditoriaInfoDTO();
        info.setPersonal(dto.getPersonal());
        info.setPlanta(dto.getPlanta());
        info.setPlantaId(dto.getPlantaId());
        info.setFecha(dto.getFecha());
        info.setTotalEnvases(dto.getTotalEnvases());
        if (dto.getContenedorAsignado() != null) {
            ContenedorInfoDTO cont = new ContenedorInfoDTO();
            cont.setUbicacion(dto.getContenedorAsignado().getUbicacion());
            cont.setCapInicial(dto.getContenedorAsignado().getCapInicial());
            cont.setEstadoEnvase(dto.getContenedorAsignado().getEstadoEnvase());
            cont.setNumEnvases(dto.getContenedorAsignado().getNumEnvases());
            info.setContenedorAsignado(cont);
        }
        return info;
    }
}
