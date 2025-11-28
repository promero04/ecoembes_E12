package com.contsocket.model;

public class ContenedorSocket {
    private Integer id;
    private Integer numEnvases;
    private String estado;

    public ContenedorSocket() {
    }

    public ContenedorSocket(Integer id, Integer numEnvases, String estado) {
        this.id = id;
        this.numEnvases = numEnvases;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumEnvases() {
        return numEnvases;
    }

    public void setNumEnvases(Integer numEnvases) {
        this.numEnvases = numEnvases;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
