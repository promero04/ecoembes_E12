package com.ecoembes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecoembes.DTO.CapacidadPlantasDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;
import com.ecoembes.entity.CapacidadPlanta;
import com.ecoembes.entity.Contenedor;
import com.ecoembes.entity.PlantaReciclaje;
import com.ecoembes.entity.RegistroAuditoria;
import com.ecoembes.repository.CapacidadPlantaRepository;
import com.ecoembes.repository.ContenedorRepository;
import com.ecoembes.repository.PlantaReciclajeRepository;
import com.ecoembes.repository.RegistroAuditoriaRepository;
import com.ecoembes.service.proxy.ContSocketProxy;
import com.ecoembes.service.proxy.PlasSbProxy;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class PlantaService {

    private final PlantaReciclajeRepository plantaReciclajeRepository;
    private final CapacidadPlantaRepository capacidadPlantaRepository;
    private final RegistroAuditoriaRepository registroAuditoriaRepository;
    private final ContenedorRepository contenedorRepository;
    private final PlasSbProxy plasSbProxy;
    private final ContSocketProxy contSocketProxy;

    public PlantaService(PlantaReciclajeRepository plantaReciclajeRepository,
            CapacidadPlantaRepository capacidadPlantaRepository,
            RegistroAuditoriaRepository registroAuditoriaRepository,
            ContenedorRepository contenedorRepository,
            PlasSbProxy plasSbProxy,
            ContSocketProxy contSocketProxy) {
        this.plantaReciclajeRepository = plantaReciclajeRepository;
        this.capacidadPlantaRepository = capacidadPlantaRepository;
        this.registroAuditoriaRepository = registroAuditoriaRepository;
        this.contenedorRepository = contenedorRepository;
        this.plasSbProxy = plasSbProxy;
        this.contSocketProxy = contSocketProxy;
    }

    @PostConstruct
    public void initData() {
        if (plantaReciclajeRepository.count() == 0) {
            registrarPlantaDemo("PlasSB Ltd.");
            registrarPlantaDemo("ContSocket Ltd.");
        }
        cargarCapacidadDemo();
    }

    private void registrarPlantaDemo(String nombre) {
        plantaReciclajeRepository.findByNombre(nombre).orElseGet(() -> plantaReciclajeRepository.save(new PlantaReciclaje(nombre)));
    }

    private void cargarCapacidadDemo() {
        LocalDate hoy = LocalDate.now();
        plantaReciclajeRepository.findAll().forEach(planta -> {
            for (int i = 0; i < 10; i++) {
                final int offset = i;
                LocalDate fecha = hoy.plusDays(offset);
                double capacidadBase = 50.0 + (offset * 2);
                capacidadPlantaRepository.findByPlantaAndFecha(planta, fecha)
                        .orElseGet(() -> capacidadPlantaRepository.save(new CapacidadPlanta(planta, fecha, capacidadBase)));
            }
        });
    }

    public List<CapacidadPlantasDTO> listarCapacidades(LocalDate fecha) {
        LocalDate target = fecha != null ? fecha : LocalDate.now();
        return capacidadPlantaRepository.findByFecha(target).stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<CapacidadPlantasDTO> getCapacidad(String nombrePlanta, LocalDate fecha) {
        Optional<PlantaReciclaje> planta = plantaReciclajeRepository.findByNombre(nombrePlanta);
        if (planta.isEmpty()) {
            return Optional.empty();
        }
        return capacidadPlantaRepository.findByPlantaAndFecha(planta.get(), fecha).map(this::toDto);
    }

    @Transactional
    public Optional<RegistroAuditoriaDTO> asignar(String nombrePlanta, List<ContenedorDTO> contenedores,
            String nombrePersonal) {
        LocalDate hoy = LocalDate.now();
        Optional<PlantaReciclaje> planta = plantaReciclajeRepository.findByNombre(nombrePlanta);
        if (planta.isEmpty()) {
            return Optional.empty();
        }
        Optional<CapacidadPlanta> capacidad = capacidadPlantaRepository.findByPlantaAndFecha(planta.get(), hoy);
        if (capacidad.isEmpty()) {
            return Optional.empty();
        }
        double capacidadDisponible = capacidad.get().getCapacidadDisponible();
        double totalEnvases = contenedores.stream().mapToDouble(ContenedorDTO::getNumEnvases).sum();
        if (totalEnvases > capacidadDisponible) {
            return Optional.empty();
        }

        capacidad.get().setCapacidadDisponible(capacidadDisponible - totalEnvases);
        capacidadPlantaRepository.save(capacidad.get());

        Contenedor contenedorAsignado = contenedores.isEmpty() ? null
                : contenedorRepository.findById(contenedores.get(0).getIdContenedor()).orElse(null);
        RegistroAuditoria registro = registroAuditoriaRepository
                .save(new RegistroAuditoria(null, nombrePlanta, contenedorAsignado, hoy, totalEnvases));

        RegistroAuditoriaDTO dto = new RegistroAuditoriaDTO();
        dto.setPlanta(nombrePlanta);
        dto.setFecha(hoy);
        dto.setTotalEnvases(totalEnvases);
        dto.setContenedorAsignado(contenedores.isEmpty() ? null : contenedores.get(0));
        dto.setPersonal(null);
        // Registrar en servicios externos
        String asignacionId = "ASG-" + registro.getId();
        plasSbProxy.registrarAsignacion(asignacionId, dto);
        contSocketProxy.registrarAsignacion(asignacionId, dto);
        return Optional.of(dto);
    }

    private CapacidadPlantasDTO toDto(CapacidadPlanta capacidad) {
        return new CapacidadPlantasDTO(capacidad.getPlanta().getNombre(), capacidad.getCapacidadDisponible());
    }
}
