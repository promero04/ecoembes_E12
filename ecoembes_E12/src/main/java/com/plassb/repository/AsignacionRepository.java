package com.plassb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plassb.entity.Asignacion;

public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    Optional<Asignacion> findByAsignacionId(String asignacionId);
}
