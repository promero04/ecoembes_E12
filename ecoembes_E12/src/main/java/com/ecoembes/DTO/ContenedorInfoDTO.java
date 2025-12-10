package com.ecoembes.DTO;

import com.ecoembes.entity.EstadoEnvase;

/**
 * DTO de salida sin identificador para exponer contenedores al cliente.
 */
public class ContenedorInfoDTO {
    private String ubicacion;
    private double capInicial;
    private EstadoEnvase estadoEnvase;
    private int numEnvases;

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
}
