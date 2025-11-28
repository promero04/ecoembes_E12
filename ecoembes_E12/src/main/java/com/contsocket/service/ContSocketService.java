package com.contsocket.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.contsocket.model.AsignacionSocket;
import com.contsocket.model.CapacidadSocket;
import com.contsocket.model.ContenedorSocket;

import jakarta.annotation.PostConstruct;

@Service
public class ContSocketService {

    private static final Logger log = LoggerFactory.getLogger(ContSocketService.class);
    private final Map<LocalDate, CapacidadSocket> capacidadesPorFecha = new ConcurrentHashMap<>();
    private final Map<String, AsignacionSocket> asignaciones = new ConcurrentHashMap<>();
    private final int socketPort;

    public ContSocketService(@Value("${external.contsocket.socket-port:9090}") int socketPort) {
        this.socketPort = socketPort;
    }

    @PostConstruct
    public void startSocketServer() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
                log.info("Servidor de sockets ContSocket escuchando en puerto {}", socketPort);
                while (true) {
                    Socket socket = serverSocket.accept();
                    handleClient(socket);
                }
            } catch (IOException ex) {
                log.error("Error en socket ContSocket: {}", ex.getMessage());
            }
        });
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line = reader.readLine();
            if (line != null && line.startsWith("ASIGNACION")) {
                log.debug("Comando socket recibido: {}", line);
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String asignacionId = parts[1];
                    double total = Double.parseDouble(parts[3]);
                    crearAsignacion(asignacionId, LocalDate.now(), "socket", total, List.of());
                }
            }
        } catch (IOException e) {
            log.error("Error leyendo socket: {}", e.getMessage());
        }
    }

    public CapacidadSocket upsertCapacidad(LocalDate fecha, double capacidadTon) {
        CapacidadSocket cap = capacidadesPorFecha.getOrDefault(fecha, new CapacidadSocket(fecha, capacidadTon));
        cap.setCapacidadTon(capacidadTon);
        capacidadesPorFecha.put(fecha, cap);
        return cap;
    }

    public Optional<CapacidadSocket> capacidad(LocalDate fecha) {
        return Optional.ofNullable(capacidadesPorFecha.get(fecha));
    }

    public AsignacionSocket crearAsignacion(String asignacionId, LocalDate fecha, String solicitante,
            double totalEnvases, List<ContenedorSocket> contenedores) {
        AsignacionSocket asignacion = new AsignacionSocket(asignacionId, fecha, solicitante, totalEnvases, "ACEPTADA");
        asignacion.getContenedores().addAll(contenedores);
        asignaciones.put(asignacionId, asignacion);
        return asignacion;
    }

    public Optional<AsignacionSocket> asignacion(String asignacionId) {
        return Optional.ofNullable(asignaciones.get(asignacionId));
    }

    public Optional<AsignacionSocket> actualizarEstado(String asignacionId, String estado, String detalle) {
        Optional<AsignacionSocket> asig = asignacion(asignacionId);
        asig.ifPresent(a -> {
            a.setEstado(estado);
            a.setDetalle(detalle);
        });
        return asig;
    }
}
