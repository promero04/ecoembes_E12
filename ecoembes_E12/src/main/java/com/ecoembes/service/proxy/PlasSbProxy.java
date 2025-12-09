package com.ecoembes.service.proxy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecoembes.DTO.CapacidadPlantasDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;

import reactor.core.publisher.Mono;

@Component
public class PlasSbProxy implements PlantaGateway {

    private static final Logger log = LoggerFactory.getLogger(PlasSbProxy.class);
    private static final String ID = "plassb";
    private final WebClient client;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    public PlasSbProxy(@Value("${external.plassb.base-url}") String baseUrl, WebClient.Builder builder) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public List<CapacidadPlantasDTO> capacidades(LocalDate fecha) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/capacidades")
                        .queryParam("fechaInicio", formatter.format(fecha != null ? fecha : LocalDate.now()))
                        .queryParam("fechaFin", formatter.format(fecha != null ? fecha : LocalDate.now()))
                        .build())
                .retrieve()
                .bodyToFlux(CapacidadResponse.class)
                .map(cap -> new CapacidadPlantasDTO("PlasSB", cap.capacidadTon()))
                .collectList()
                .onErrorResume(ex -> {
                    log.warn("Fallo consultando capacidades PlasSB: {}", ex.getMessage());
                    return Mono.just(List.of());
                })
                .block();
    }

    @Override
    public boolean registrarAsignacion(String asignacionId, RegistroAuditoriaDTO dto) {
        var payload = new AsignacionRequest(asignacionId, dto.getFecha(), dto.getPersonal() != null ? dto.getPersonal().getCorreo() : "ecoembes",
                dto.getTotalEnvases(),
                dto.getContenedorAsignado() == null ? List.of() : List.of(dto.getContenedorAsignado()));
        Boolean ok = client.post()
                .uri("/asignaciones")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(AsignacionResponse.class)
                .map(resp -> resp.estado().equalsIgnoreCase("ACEPTADA") || resp.estado().equalsIgnoreCase("COMPLETADA"))
                .onErrorResume(ex -> {
                    log.warn("No se pudo registrar la asignacion en PlasSB: {}", ex.getMessage());
                    return Mono.just(false);
                })
                .block();
        return Boolean.TRUE.equals(ok);
    }

    public record AsignacionRequest(String asignacionId, LocalDate fecha, String solicitante, double totalEnvases,
            List<ContenedorDTO> contenedores) {
    }

    public record AsignacionResponse(String asignacionId, String estado, String mensaje) {
    }

    public record CapacidadResponse(LocalDate fecha, double capacidadTon) {
    }

    @Override
    public Optional<CapacidadPlantasDTO> capacidad(LocalDate fecha) {
        return capacidades(fecha).stream().findFirst();
    }
}
