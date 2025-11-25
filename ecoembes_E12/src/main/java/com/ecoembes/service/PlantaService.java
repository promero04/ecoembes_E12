package com.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ecoembes.DTO.CapacidadPlantasDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;
import com.ecoembes.entity.PlantaReciclaje;
import com.ecoembes.entity.RegistroAuditoria;

@Service
public class PlantaService {

    private final Map<String, PlantaReciclaje> plantas = new ConcurrentHashMap<>();
    private final Map<String, Map<LocalDate, Double>> capacidadPorFecha = new ConcurrentHashMap<>();
    private final List<RegistroAuditoria> auditoria = new ArrayList<>();

    public PlantaService() {
        registrarPlantaDemo("PlasSB Ltd.");
        registrarPlantaDemo("ContSocket Ltd.");
        cargarCapacidadDemo();
    }

    private void registrarPlantaDemo(String nombre) {
        plantas.put(nombre, new PlantaReciclaje(nombre));
    }

    private void cargarCapacidadDemo() {
        LocalDate hoy = LocalDate.now();
        plantas.keySet().forEach(nombre -> {
            Map<LocalDate, Double> capacidades = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                capacidades.put(hoy.plusDays(i), 50.0 + (i * 2));
            }
            capacidadPorFecha.put(nombre, capacidades);
        });
    }

    public List<CapacidadPlantasDTO> listarCapacidades(LocalDate fecha) {
        LocalDate target = fecha != null ? fecha : LocalDate.now();
        return plantas.keySet().stream()
                .map(nombre -> getCapacidad(nombre, target).orElse(null))
                .filter(dto -> dto != null)
                .toList();
    }

    public Optional<CapacidadPlantasDTO> getCapacidad(String nombrePlanta, LocalDate fecha) {
        PlantaReciclaje planta = plantas.get(nombrePlanta);
        if (planta == null) {
            return Optional.empty();
        }
        Map<LocalDate, Double> capacidades = capacidadPorFecha.getOrDefault(nombrePlanta, Map.of());
        Double capacidad = capacidades.get(fecha);
        if (capacidad == null) {
            return Optional.empty();
        }
        return Optional.of(new CapacidadPlantasDTO(planta.getNombre(), capacidad));
    }

    public Optional<RegistroAuditoriaDTO> asignar(String nombrePlanta, List<ContenedorDTO> contenedores,
            String nombrePersonal) {
        LocalDate hoy = LocalDate.now();
        Optional<CapacidadPlantasDTO> capacidadDTO = getCapacidad(nombrePlanta, hoy);
        if (capacidadDTO.isEmpty()) {
            return Optional.empty();
        }
        double capacidadDisponible = capacidadDTO.get().getCapacidadTotal();
        double totalEnvases = contenedores.stream().mapToDouble(ContenedorDTO::getNumEnvases).sum();
        if (totalEnvases > capacidadDisponible) {
            return Optional.empty();
        }
        // Reducimos la capacidad disponible del día como simulación.
        capacidadPorFecha.get(nombrePlanta).put(hoy, capacidadDisponible - totalEnvases);

        RegistroAuditoria registro = new RegistroAuditoria(null, nombrePlanta, null, hoy, totalEnvases);
        auditoria.add(registro);

        RegistroAuditoriaDTO dto = new RegistroAuditoriaDTO();
        dto.setPlanta(nombrePlanta);
        dto.setFecha(hoy);
        dto.setTotalEnvases(totalEnvases);
        dto.setContenedorAsignado(contenedores.isEmpty() ? null : contenedores.get(0));
        return Optional.of(dto);
    }
}
