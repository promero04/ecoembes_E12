package com.ecoembes.service.proxy;

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

import com.ecoembes.DTO.CapacidadPlantasDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;

import reactor.core.publisher.Mono;

@Component
public class ContSocketProxy {

    private static final Logger log = LoggerFactory.getLogger(ContSocketProxy.class);
    private final WebClient client;
    private final String socketHost;
    private final int socketPort;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    public ContSocketProxy(@Value("${external.contsocket.base-url}") String baseUrl,
            @Value("${external.contsocket.socket-host}") String socketHost,
            @Value("${external.contsocket.socket-port}") int socketPort,
            WebClient.Builder builder) {
        this.client = builder.baseUrl(baseUrl).build();
        this.socketHost = socketHost;
        this.socketPort = socketPort;
    }

    public Optional<CapacidadPlantasDTO> capacidad(LocalDate fecha) {
        return client.get()
                .uri("/capacidades/{fecha}", formatter.format(fecha))
                .retrieve()
                .bodyToMono(CapacidadResponse.class)
                .map(resp -> new CapacidadPlantasDTO("ContSocket", resp.capacidadTon()))
                .onErrorResume(ex -> {
                    log.warn("No se pudo leer capacidad en ContSocket: {}", ex.getMessage());
                    return Mono.empty();
                })
                .blockOptional();
    }

    public boolean registrarAsignacion(String asignacionId, RegistroAuditoriaDTO dto) {
        var payload = new AsignacionRequest(asignacionId, dto.getFecha(),
                dto.getPersonal() != null ? dto.getPersonal().getCorreo() : "ecoembes",
                dto.getTotalEnvases(),
                dto.getContenedorAsignado() == null ? List.of() : List.of(dto.getContenedorAsignado()));
        Boolean ok = client.post()
                .uri("/asignaciones")
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
            writer.printf("ASIGNACION|%s|%s|%f%n", asignacionId, dto.getPlanta(), dto.getTotalEnvases());
            return true;
        } catch (IOException ex) {
            log.error("No se pudo registrar via socket en ContSocket: {}", ex.getMessage());
            return false;
        }
    }

    public record AsignacionRequest(String asignacionId, LocalDate fecha, String solicitante, double totalEnvases,
            List<ContenedorDTO> contenedores) {
    }

    public record AsignacionResponse(String asignacionId, String estado, String mensaje) {
    }

    public record CapacidadResponse(LocalDate fecha, double capacidadTon) {
    }
}
