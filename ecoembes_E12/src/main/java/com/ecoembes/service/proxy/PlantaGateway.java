package com.ecoembes.service.proxy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.ecoembes.DTO.CapacidadPlantasDTO;
import com.ecoembes.DTO.RegistroAuditoriaDTO;

/**
 * Puerta de enlace genérica hacia servicios de plantas de reciclaje.
 * Implementaciones concretas: PlasSB y ContSocket.
 */
public interface PlantaGateway {

    /**
     * Identificador único del gateway (ej: "plassb", "contsocket").
     */
    String id();

    /**
     * Recupera la capacidad de la planta asociada para la fecha indicada.
     */
    Optional<CapacidadPlantasDTO> capacidad(LocalDate fecha);

    /**
     * Recupera capacidades (puede devolver lista de un único elemento).
     */
    List<CapacidadPlantasDTO> capacidades(LocalDate fecha);

    /**
     * Registra una asignación de contenedores en el servicio externo.
     */
    boolean registrarAsignacion(String asignacionId, RegistroAuditoriaDTO dto);
}
