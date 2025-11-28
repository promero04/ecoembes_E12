package com.ecoembes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoembes.entity.Personal;

public interface PersonalRepository extends JpaRepository<Personal, Integer> {

    Optional<Personal> findByCorreo(String correo);
}
