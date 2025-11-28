package com.plassb.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plassb.entity.Capacidad;

public interface CapacidadRepository extends JpaRepository<Capacidad, Long> {

    Optional<Capacidad> findByFecha(LocalDate fecha);
}
