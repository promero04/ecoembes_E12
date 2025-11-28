package com.plassb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "psb_asignacion")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asignacion_id", nullable = false, unique = true)
    private String asignacionId;

    private LocalDate fecha;

    private String solicitante;

    private double totalEnvases;

    private String estado;

    private String detalle;

    @OneToMany(mappedBy = "asignacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContenedorAsignado> contenedores = new ArrayList<>();

    public Asignacion() {
    }

    public Asignacion(String asignacionId, LocalDate fecha, String solicitante, double totalEnvases, String estado) {
        this.asignacionId = asignacionId;
        this.fecha = fecha;
        this.solicitante = solicitante;
        this.totalEnvases = totalEnvases;
        this.estado = estado;
    }

    public void addContenedor(ContenedorAsignado contenedor) {
        contenedor.setAsignacion(this);
        this.contenedores.add(contenedor);
    }

    public Long getId() {
        return id;
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

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDetalle() {
        return detalle;
    }

    public List<ContenedorAsignado> getContenedores() {
        return contenedores;
    }
}
