package com.ecoembes.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.entity.Contenedor;
import com.ecoembes.entity.EstadoEnvase;
import com.ecoembes.entity.Lectura;
import com.ecoembes.repository.ContenedorRepository;
import com.ecoembes.repository.LecturaRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;
    private final LecturaRepository lecturaRepository;

    public ContenedorService(ContenedorRepository contenedorRepository, LecturaRepository lecturaRepository) {
        this.contenedorRepository = contenedorRepository;
        this.lecturaRepository = lecturaRepository;
    }

    @PostConstruct
    public void initData() {
        if (contenedorRepository.count() == 0) {
            crear(new ContenedorDTO(0, "Calle Mayor 1, 28001", 120.0, EstadoEnvase.VERDE, 10));
            crear(new ContenedorDTO(0, "Calle Alcala 45, 28014", 150.0, EstadoEnvase.NARANJA, 90));
            crear(new ContenedorDTO(0, "Calle Serrano 90, 28006", 180.0, EstadoEnvase.ROJO, 180));
        }
    }

    public Collection<ContenedorDTO> listar() {
        return contenedorRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<ContenedorDTO> obtener(int id) {
        return contenedorRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public ContenedorDTO crear(ContenedorDTO dto) {
        Contenedor contenedor = new Contenedor(dto.getUbicacion(), dto.getCapInicial(),
                dto.getEstadoEnvase() == null ? EstadoEnvase.VERDE : dto.getEstadoEnvase(),
                dto.getNumEnvases());
        Contenedor guardado = contenedorRepository.save(contenedor);
        lecturaRepository.save(new Lectura(LocalDate.now(), dto.getNumEnvases(), guardado.getNEstadoEnvase(), guardado));
        return toDto(guardado);
    }

    @Transactional
    public Optional<ContenedorDTO> actualizarSensor(int id, int numEnvases, EstadoEnvase estado) {
        Optional<Contenedor> opt = contenedorRepository.findById(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        Contenedor contenedor = opt.get();
        contenedor.setNumLlenado(numEnvases);
        contenedor.setNEstadoEnvase(estado);
        Contenedor actualizado = contenedorRepository.save(contenedor);
        lecturaRepository.save(new Lectura(LocalDate.now(), numEnvases, estado, actualizado));
        return Optional.of(toDto(actualizado));
    }

    public List<ContenedorDTO> buscarPorZona(String zona) {
        if (zona == null || zona.isBlank()) {
            return listar().stream().toList();
        }
        return contenedorRepository.findByUbicacionContainingIgnoreCase(zona)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ContenedorDTO> buscarPorFechaYZona(LocalDate fecha, String zona) {
        if (fecha == null) {
            return buscarPorZona(zona);
        }
        return contenedorRepository.findByFechaAndZona(fecha, zona)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ContenedorDTO> consultarUso(int id, LocalDate inicio, LocalDate fin) {
        List<Lectura> lecturas = lecturaRepository.findByContenedorIdContenedorAndFechaBetween(id, inicio, fin);
        return lecturas.stream().map(l -> {
            ContenedorDTO dto = new ContenedorDTO();
            dto.setIdContenedor(id);
            dto.setNumEnvases(l.getTotalEnvases());
            dto.setEstadoEnvase(l.getNivelLlenado());
            contenedorRepository.findById(id).ifPresent(c -> {
                dto.setUbicacion(c.getUbicacion());
                dto.setCapInicial(c.getCapInicial());
            });
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
}
