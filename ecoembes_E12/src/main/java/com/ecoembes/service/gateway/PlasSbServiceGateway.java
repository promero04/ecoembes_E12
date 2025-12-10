package com.ecoembes.service.gateway;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecoembes.DTO.CapacidadPlantaDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;

import reactor.core.publisher.Mono;

@Component
public class PlasSbServiceGateway implements PlantaGateway {

    private static final Logger log = LoggerFactory.getLogger(PlasSbServiceGateway.class);
    private static final String ID = "plassb";
    private final WebClient client;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    public PlasSbServiceGateway(@Value("${external.plassb.base-url}") String baseUrl, WebClient.Builder builder) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public List<CapacidadPlantaDTO> capacidades(LocalDate fecha) {
        LocalDate target = fecha != null ? fecha : LocalDate.now();
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/capacidad")
                        .queryParam("fechaInicio", formatter.format(target))
                        .queryParam("fechaFin", formatter.format(target))
                        .build())
                .retrieve()
                .bodyToFlux(CapacidadResponse.class)
                .map(cap -> new CapacidadPlantaDTO("PlasSB", cap.capacidadTon()))
                .collectList()
                .onErrorResume(ex -> {
                    log.warn("Fallo consultando capacidades PlasSB: {}", ex.getMessage());
                    return Mono.just(List.of());
                })
                .block();
    }

    @Override
    public boolean registrarAsignacion(String asignacionId, RegistroAuditoriaDTO dto) {
        var contenedores = dto.getContenedorAsignado() == null ? List.<ContenedorAsignacionPayload>of()
                : List.of(new ContenedorAsignacionPayload(
                        dto.getContenedorAsignado().getIdContenedor(),
                        dto.getContenedorAsignado().getNumEnvases(),
                        dto.getContenedorAsignado().getEstadoEnvase().name()));
        var payload = new AsignacionRequest(asignacionId, dto.getPlantaId(),
                dto.getPersonal() != null ? dto.getPersonal().getCorreo() : "ecoembes",
                dto.getTotalEnvases(),
                contenedores);
        Boolean ok = client.post()
                .uri("/asignacion")
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

    public record AsignacionRequest(String asignacionId, Long plantaId, String solicitante, double totalEnvases,
            List<ContenedorAsignacionPayload> contenedores) {
    }

    public record AsignacionResponse(String asignacionId, String estado, String mensaje) {
    }

    public record ContenedorAsignacionPayload(Integer id, Integer numEnvases, String estado) {
    }

    public record CapacidadResponse(LocalDate fecha, double capacidadTon) {
    }

    @Override
    public Optional<CapacidadPlantaDTO> capacidad(LocalDate fecha) {
        return capacidades(fecha).stream().findFirst();
    }
}
