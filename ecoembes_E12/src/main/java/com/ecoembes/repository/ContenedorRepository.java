package com.ecoembes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecoembes.entity.Contenedor;

public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {

    List<Contenedor> findByUbicacionContainingIgnoreCase(String zona);

    @Query("""
            select distinct l.contenedor from Lectura l
            where l.fecha = :fecha
              and (:zona is null or lower(l.contenedor.ubicacion) like lower(concat('%', :zona, '%')))
            """)
    List<Contenedor> findByFechaAndZona(@Param("fecha") LocalDate fecha, @Param("zona") String zona);
}
