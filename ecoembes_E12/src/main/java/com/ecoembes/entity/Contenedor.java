package com.ecoembes.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contenedor")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenedor")
    private Integer idContenedor;

    @Column(nullable = false)
    private String ubicacion;

    @Column(name = "capacidad_inicial")
    private double capInicial;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envase")
    private EstadoEnvase estadoEnvase; // Lo mismo que nivelLlenado

    @Column(name = "numero_envases")
    private int numLlenado;

    protected Contenedor() {
        // JPA
    }

    public Contenedor(String ubicacion, double capInicial, EstadoEnvase estadoEnvase, int numLlenado) {
        this.ubicacion = ubicacion;
        this.capInicial = capInicial;
        this.estadoEnvase = estadoEnvase == null ? EstadoEnvase.VERDE : estadoEnvase;
        this.numLlenado = numLlenado;
    }

    public Integer getIdContenedor() {
        return idContenedor;
    }

    public void setIdContenedor(Integer idContenedor) {
        this.idContenedor = idContenedor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getCapInicial() {
        return capInicial;
    }

    public void setCapInicial(double capInicial) {
        this.capInicial = capInicial;
    }

    public EstadoEnvase getNEstadoEnvase() {
        return estadoEnvase;
    }

    public void setNEstadoEnvase(EstadoEnvase nEstadoEnvase) {
        this.estadoEnvase = nEstadoEnvase;
    }

    public int getNumLlenado() {
        return numLlenado;
    }

    public void setNumLlenado(int numLlenado) {
        this.numLlenado = numLlenado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(capInicial, ubicacion, idContenedor, estadoEnvase, numLlenado);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contenedor other = (Contenedor) obj;
        return Double.doubleToLongBits(capInicial) == Double.doubleToLongBits(other.capInicial)
                && numLlenado == other.numLlenado && Objects.equals(idContenedor, other.idContenedor)
                && Objects.equals(ubicacion, other.ubicacion);
    }
}
