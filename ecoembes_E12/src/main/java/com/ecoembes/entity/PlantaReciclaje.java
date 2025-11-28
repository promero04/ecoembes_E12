package com.ecoembes.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "planta_reciclaje")
public class PlantaReciclaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(name = "capacidad_disponible")
    private Double capacidadDisponible;

    protected PlantaReciclaje() {
        // JPA
    }

    public PlantaReciclaje(String nombre) {
        this.nombre = nombre;
        this.capacidadDisponible = 0.0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCapacidadDisponible() {
        return capacidadDisponible;
    }

    public void setCapacidadDisponible(Double capacidadDisponible) {
        this.capacidadDisponible = capacidadDisponible;
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacidadDisponible, id, nombre);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlantaReciclaje other = (PlantaReciclaje) obj;
        return Objects.equals(capacidadDisponible, other.capacidadDisponible) && Objects.equals(id, other.id)
                && Objects.equals(nombre, other.nombre);
    }
}
