package com.ecoembes.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoembes.entity.CapacidadPlanta;
import com.ecoembes.entity.PlantaReciclaje;

public interface CapacidadPlantaRepository extends JpaRepository<CapacidadPlanta, Long> {

    Optional<CapacidadPlanta> findByPlantaAndFecha(PlantaReciclaje planta, LocalDate fecha);

    List<CapacidadPlanta> findByFecha(LocalDate fecha);
}
