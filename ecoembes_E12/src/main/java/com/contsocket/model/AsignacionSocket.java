package com.contsocket.model;

import java.util.ArrayList;
import java.util.List;

public class AsignacionSocket {
    private String asignacionId;
    private Long plantaId;
    private String solicitante;
    private double totalEnvases;
    private String estado;
    private List<ContenedorSocket> contenedores = new ArrayList<>();

    public AsignacionSocket(String asignacionId, Long plantaId, String solicitante, double totalEnvases,
            String estado) {
        this.asignacionId = asignacionId;
        this.plantaId = plantaId;
        this.solicitante = solicitante;
        this.totalEnvases = totalEnvases;
        this.estado = estado;
    }

    public String getAsignacionId() {
        return asignacionId;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public double getTotalEnvases() {
        return totalEnvases;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ContenedorSocket> getContenedores() {
        return contenedores;
    }

    public Long getPlantaId() {
        return plantaId;
    }

    public void setPlantaId(Long plantaId) {
        this.plantaId = plantaId;
    }
}
