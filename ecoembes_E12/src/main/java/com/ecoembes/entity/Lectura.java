package com.ecoembes.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lectura")
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "total_envases")
    private int totalEnvases;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_llenado")
    private EstadoEnvase nivelLlenado;

    @ManyToOne
    @JoinColumn(name = "contenedor_id")
    private Contenedor contenedor;

    protected Lectura() {
        // JPA
    }

    public Lectura(LocalDate fecha, int totalEnvases, EstadoEnvase nivelLlenado, Contenedor contenedor) {
        this.fecha = fecha;
        this.totalEnvases = totalEnvases;
        this.nivelLlenado = nivelLlenado;
        this.contenedor = contenedor;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getTotalEnvases() {
        return totalEnvases;
    }

    public EstadoEnvase getNivelLlenado() {
        return nivelLlenado;
    }

    public Contenedor getContenedor() {
        return contenedor;
    }
}
