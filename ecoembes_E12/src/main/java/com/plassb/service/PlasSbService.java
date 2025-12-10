package com.plassb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.plassb.entity.Asignacion;
import com.plassb.entity.Capacidad;
import com.plassb.entity.ContenedorAsignado;
import com.plassb.repository.AsignacionRepository;
import com.plassb.repository.CapacidadRepository;

@Service
public class PlasSbService {

    private final CapacidadRepository capacidadRepository;
    private final AsignacionRepository asignacionRepository;

    public PlasSbService(CapacidadRepository capacidadRepository, AsignacionRepository asignacionRepository) {
        this.capacidadRepository = capacidadRepository;
        this.asignacionRepository = asignacionRepository;
    }

    public Capacidad upsertCapacidad(LocalDate fecha, double capacidadTon) {
        Capacidad capacidad = capacidadRepository.findByFecha(fecha)
                .orElse(new Capacidad(fecha, capacidadTon));
        capacidad.setCapacidadTon(capacidadTon);
        return capacidadRepository.save(capacidad);
    }

    public Optional<Capacidad> capacidad(LocalDate fecha) {
        return capacidadRepository.findByFecha(fecha);
    }

    public List<Capacidad> capacidades(LocalDate inicio, LocalDate fin) {
        LocalDate start = inicio != null ? inicio : LocalDate.now();
        LocalDate end = fin != null ? fin : start;
        return capacidadRepository.findAll().stream()
                .filter(c -> !c.getFecha().isBefore(start) && !c.getFecha().isAfter(end))
                .toList();
    }

    public Asignacion crearAsignacion(String asignacionId, Long plantaId, String solicitante, double totalEnvases,
            List<ContenedorAsignado> contenedores) {
        Asignacion asignacion = new Asignacion(asignacionId, plantaId, solicitante, totalEnvases, "ACEPTADA");
        contenedores.forEach(asignacion::addContenedor);
        return asignacionRepository.save(asignacion);
    }

    public List<Asignacion> asignaciones(Long plantaId) {
        if (plantaId == null) {
            return asignacionRepository.findAll();
        }
        return asignacionRepository.findByPlantaId(plantaId);
    }

    public Optional<Asignacion> asignacionPorId(String asignacionId) {
        return asignacionRepository.findByAsignacionId(asignacionId);
    }

    public Optional<Asignacion> actualizarEstado(String asignacionId, String estado) {
        Optional<Asignacion> asig = asignacionRepository.findByAsignacionId(asignacionId);
        asig.ifPresent(a -> {
            a.setEstado(estado);
            asignacionRepository.save(a);
        });
        return asig;
    }
}
