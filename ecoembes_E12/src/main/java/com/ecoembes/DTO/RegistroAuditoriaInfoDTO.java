package com.ecoembes.DTO;

import java.time.LocalDate;

/**
 * DTO de salida para auditorías sin exponer identificadores internos.
 */
public class RegistroAuditoriaInfoDTO {
    private PersonalDTO personal;
    private Long plantaId;
    private String planta;
    private ContenedorInfoDTO contenedorAsignado;
    private LocalDate fecha;
    private double totalEnvases;

    public PersonalDTO getPersonal() {
        return personal;
    }

    public void setPersonal(PersonalDTO personal) {
        this.personal = personal;
    }

    public Long getPlantaId() {
        return plantaId;
    }

    public void setPlantaId(Long plantaId) {
        this.plantaId = plantaId;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public ContenedorInfoDTO getContenedorAsignado() {
        return contenedorAsignado;
    }

    public void setContenedorAsignado(ContenedorInfoDTO contenedorAsignado) {
        this.contenedorAsignado = contenedorAsignado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotalEnvases() {
        return totalEnvases;
    }

    public void setTotalEnvases(double totalEnvases) {
        this.totalEnvases = totalEnvases;
    }
}
