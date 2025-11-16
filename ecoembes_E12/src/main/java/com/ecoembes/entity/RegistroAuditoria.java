package com.ecoembes.entity;

import java.time.LocalDate;

public class RegistroAuditoria {
    protected Personal personal; 
    protected String planta;
    protected Contenedor contenedoresAsignados;
    protected LocalDate fecha;
    protected double totalEnvases;

    public RegistroAuditoria(Personal personal, String planta, Contenedor contenedoresAsignados, LocalDate fecha, double totalEnvases) {
        super();
        this.personal = personal;
        this.planta = planta;
        this.contenedoresAsignados = contenedoresAsignados;
        this.fecha = fecha;
        this.totalEnvases = totalEnvases;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public Contenedor getContenedoresAsignados() {
        return contenedoresAsignados;
    }

    public void setContenedoresAsignados(Contenedor contenedoresAsignados) {
        this.contenedoresAsignados = contenedoresAsignados;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotalEnvases() {
        return totalEnvases;
    }

    public void setTotalEnvases(double totalEnvases) {
        this.totalEnvases = totalEnvases;
    }

}
