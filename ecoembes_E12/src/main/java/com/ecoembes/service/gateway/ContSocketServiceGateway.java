package com.ecoembes.service.gateway;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
public class ContSocketServiceGateway implements PlantaGateway {

    private static final Logger log = LoggerFactory.getLogger(ContSocketServiceGateway.class);
    private static final String ID = "contsocket";
    private final WebClient client;
    private final String socketHost;
    private final int socketPort;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    public ContSocketServiceGateway(@Value("${external.contsocket.base-url}") String baseUrl,
            @Value("${external.contsocket.socket-host}") String socketHost,
            @Value("${external.contsocket.socket-port}") int socketPort,
            WebClient.Builder builder) {
        this.client = builder.baseUrl(baseUrl).build();
        this.socketHost = socketHost;
        this.socketPort = socketPort;
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public Optional<CapacidadPlantaDTO> capacidad(LocalDate fecha) {
        LocalDate target = fecha != null ? fecha : LocalDate.now();
        return client.get()
                .uri("/capacidad/{fecha}", formatter.format(target))
                .retrieve()
                .bodyToMono(CapacidadResponse.class)
                .map(resp -> new CapacidadPlantaDTO("ContSocket", resp.capacidadTon()))
                .onErrorResume(ex -> {
                    log.warn("No se pudo leer capacidad en ContSocket: {}", ex.getMessage());
                    return Mono.empty();
                })
                .blockOptional();
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
                    log.warn("Fallo REST ContSocket, se intenta canal socket: {}", ex.getMessage());
                    return Mono.just(fallbackSocket(asignacionId, dto));
                })
                .block();
        return Boolean.TRUE.equals(ok);
    }

    private boolean fallbackSocket(String asignacionId, RegistroAuditoriaDTO dto) {
        try (Socket socket = new Socket(socketHost, socketPort);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            long plantaId = dto.getPlantaId() != null ? dto.getPlantaId() : 0L;
            writer.printf("ASIGNACION|%s|%d|%f%n", asignacionId, plantaId, dto.getTotalEnvases());
            return true;
        } catch (IOException ex) {
            log.error("No se pudo registrar via socket en ContSocket: {}", ex.getMessage());
            return false;
        }
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
    public List<CapacidadPlantaDTO> capacidades(LocalDate fecha) {
        return capacidad(fecha).map(List::of).orElseGet(List::of);
    }
}
