package com.contsocket.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsignacionSocket {
    private String asignacionId;
    private LocalDate fecha;
    private String solicitante;
    private double totalEnvases;
    private String estado;
    private String detalle;
    private List<ContenedorSocket> contenedores = new ArrayList<>();

    public AsignacionSocket(String asignacionId, LocalDate fecha, String solicitante, double totalEnvases,
            String estado) {
        this.asignacionId = asignacionId;
        this.fecha = fecha;
        this.solicitante = solicitante;
        this.totalEnvases = totalEnvases;
        this.estado = estado;
    }

    public String getAsignacionId() {
        return asignacionId;
    }

    public LocalDate getFecha() {
        return fecha;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public List<ContenedorSocket> getContenedores() {
        return contenedores;
    }
}
