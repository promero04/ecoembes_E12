package com.ecoembes.DTO;

import java.util.Objects;

import com.ecoembes.entity.EstadoEnvase;

public class ContenedorDTO {

    private int idContenedor;
    private String ubicacion;
    private double capInicial;
    private EstadoEnvase estadoEnvase;
    private int numEnvases;

    public ContenedorDTO() {
        // Constructor sin argumentos necesario para la deserializacion.
    }

    public ContenedorDTO(int idContenedor, String ubicacion, double capInicial, EstadoEnvase estadoEnvase,
            int numEnvases) {
        this.idContenedor = idContenedor;
        this.ubicacion = ubicacion;
        this.capInicial = capInicial;
        this.estadoEnvase = estadoEnvase;
        this.numEnvases = numEnvases;
    }

    public int getIdContenedor() {
        return idContenedor;
    }

    public void setIdContenedor(int idContenedor) {
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

    public EstadoEnvase getEstadoEnvase() {
        return estadoEnvase;
    }

    public void setEstadoEnvase(EstadoEnvase estadoEnvase) {
        this.estadoEnvase = estadoEnvase;
    }

    public int getNumEnvases() {
        return numEnvases;
    }

    public void setNumEnvases(int numEnvases) {
        this.numEnvases = numEnvases;
    }

    @Override
    public int hashCode() {
        return Objects.hash(capInicial, estadoEnvase, idContenedor, numEnvases, ubicacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ContenedorDTO other = (ContenedorDTO) obj;
        return Double.doubleToLongBits(capInicial) == Double.doubleToLongBits(other.capInicial)
                && idContenedor == other.idContenedor
                && numEnvases == other.numEnvases
                && estadoEnvase == other.estadoEnvase
                && Objects.equals(ubicacion, other.ubicacion);
    }
}
