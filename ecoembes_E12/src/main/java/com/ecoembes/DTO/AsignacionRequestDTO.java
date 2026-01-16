package com.ecoembes.DTO;

import java.util.ArrayList;
import java.util.List;

public class AsignacionRequestDTO {

    private String planta;
    private List<Integer> contenedores = new ArrayList<>();

    public AsignacionRequestDTO() {
        // Constructor sin argumentos necesario para la deserializacion.
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public List<Integer> getContenedores() {
        return contenedores;
    }

    public void setContenedores(List<Integer> contenedores) {
        this.contenedores = contenedores != null ? contenedores : new ArrayList<>();
    }
}
