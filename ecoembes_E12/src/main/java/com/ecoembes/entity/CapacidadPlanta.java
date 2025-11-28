package com.ecoembes.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "capacidad_planta")
public class CapacidadPlanta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "planta_id")
    private PlantaReciclaje planta;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "capacidad_disponible")
    private double capacidadDisponible;

    protected CapacidadPlanta() {
        // JPA
    }

    public CapacidadPlanta(PlantaReciclaje planta, LocalDate fecha, double capacidadDisponible) {
        this.planta = planta;
        this.fecha = fecha;
        this.capacidadDisponible = capacidadDisponible;
    }

    public Long getId() {
        return id;
    }

    public PlantaReciclaje getPlanta() {
        return planta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getCapacidadDisponible() {
        return capacidadDisponible;
    }

    public void setCapacidadDisponible(double capacidadDisponible) {
        this.capacidadDisponible = capacidadDisponible;
    }
}
