package com.ecoembes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoembes.entity.PlantaReciclaje;

public interface PlantaReciclajeRepository extends JpaRepository<PlantaReciclaje, Integer> {

    Optional<PlantaReciclaje> findByNombre(String nombre);
}
