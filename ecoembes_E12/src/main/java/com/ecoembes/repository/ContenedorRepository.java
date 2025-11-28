package com.ecoembes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoembes.entity.Contenedor;

public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {

    List<Contenedor> findByUbicacionContainingIgnoreCase(String zona);
}
