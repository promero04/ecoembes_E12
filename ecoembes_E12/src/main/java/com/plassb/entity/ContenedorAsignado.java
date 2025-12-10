package com.plassb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "psb_contenedor_asignado")
public class ContenedorAsignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contenedor_id")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer contenedorId;

    @Column(name = "num_envases")
    private Integer numEnvases;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "asignacion_id")
    @JsonIgnore
    private Asignacion asignacion;

    public ContenedorAsignado() {
    }

    public ContenedorAsignado(Integer contenedorId, Integer numEnvases, String estado) {
        this.contenedorId = contenedorId;
        this.numEnvases = numEnvases;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public Integer getContenedorId() {
        return contenedorId;
    }

    public Integer getNumEnvases() {
        return numEnvases;
    }

    public String getEstado() {
        return estado;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }
}
