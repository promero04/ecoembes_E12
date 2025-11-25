package com.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.entity.Contenedor;
import com.ecoembes.entity.EstadoEnvase;

@Service
public class ContenedorService {

    private final Map<Integer, Contenedor> contenedores = new ConcurrentHashMap<>();
    private final Map<Integer, List<SensorLectura>> historico = new ConcurrentHashMap<>();

    public ContenedorService() {
        // Datos de ejemplo
        crear(new ContenedorDTO(100, "Calle Mayor 1, 28001", 120.0, EstadoEnvase.VERDE, 10));
        crear(new ContenedorDTO(101, "Calle Alcalá 45, 28014", 150.0, EstadoEnvase.NARANJA, 90));
        crear(new ContenedorDTO(102, "Calle Serrano 90, 28006", 180.0, EstadoEnvase.ROJO, 180));
    }

    public Collection<ContenedorDTO> listar() {
        return contenedores.values().stream().map(this::toDto).toList();
    }

    public Optional<ContenedorDTO> obtener(int id) {
        return Optional.ofNullable(contenedores.get(id)).map(this::toDto);
    }

    public ContenedorDTO crear(ContenedorDTO dto) {
        Contenedor contenedor = new Contenedor(dto.getUbicacion(), dto.getCapInicial(),
                dto.getEstadoEnvase() == null ? EstadoEnvase.VERDE : dto.getEstadoEnvase(),
                dto.getNumEnvases());
        contenedor.setIdContenedor(dto.getIdContenedor());
        contenedores.put(contenedor.getIdContenedor(), contenedor);
        historico.putIfAbsent(contenedor.getIdContenedor(), new ArrayList<>());
        historico.get(contenedor.getIdContenedor())
                .add(new SensorLectura(LocalDate.now(), dto.getNumEnvases(), contenedor.getNEstadoEnvase()));
        return toDto(contenedor);
    }

    public Optional<ContenedorDTO> actualizarSensor(int id, int numEnvases, EstadoEnvase estado) {
        Contenedor contenedor = contenedores.get(id);
        if (contenedor == null) {
            return Optional.empty();
        }
        contenedor.setNumLlenado(numEnvases);
        contenedor.setNEstadoEnvase(estado);
        historico.computeIfAbsent(id, k -> new ArrayList<>())
                .add(new SensorLectura(LocalDate.now(), numEnvases, estado));
        return Optional.of(toDto(contenedor));
    }

    public List<ContenedorDTO> buscarPorZona(String zona) {
        if (zona == null || zona.isBlank()) {
            return listar().stream().toList();
        }
        String criterio = zona.toLowerCase();
        return contenedores.values().stream()
                .filter(c -> c.getUbicacion() != null && c.getUbicacion().toLowerCase().contains(criterio))
                .map(this::toDto)
                .toList();
    }

    public List<ContenedorDTO> consultarUso(int id, LocalDate inicio, LocalDate fin) {
        List<SensorLectura> lecturas = historico.getOrDefault(id, List.of());
        return lecturas.stream()
                .filter(l -> !l.fecha.isBefore(inicio) && !l.fecha.isAfter(fin))
                .map(l -> {
                    ContenedorDTO dto = new ContenedorDTO();
                    dto.setIdContenedor(id);
                    dto.setNumEnvases(l.numEnvases);
                    dto.setEstadoEnvase(l.estado);
                    dto.setUbicacion(contenedores.get(id) != null ? contenedores.get(id).getUbicacion() : null);
                    dto.setCapInicial(contenedores.get(id) != null ? contenedores.get(id).getCapInicial() : 0);
                    return dto;
                }).toList();
    }

    private ContenedorDTO toDto(Contenedor contenedor) {
        ContenedorDTO dto = new ContenedorDTO();
        dto.setIdContenedor(contenedor.getIdContenedor());
        dto.setUbicacion(contenedor.getUbicacion());
        dto.setCapInicial(contenedor.getCapInicial());
        dto.setEstadoEnvase(contenedor.getNEstadoEnvase());
        dto.setNumEnvases(contenedor.getNumLlenado());
        return dto;
    }

    private record SensorLectura(LocalDate fecha, int numEnvases, EstadoEnvase estado) {
    }
}
