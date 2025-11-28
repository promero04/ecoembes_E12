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
@Table(name = "registro_auditoria")
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personal_id")
    protected Personal personal;

    @Column(nullable = false)
    protected String planta;

    @ManyToOne
    @JoinColumn(name = "contenedor_id")
    protected Contenedor contenedoresAsignados;

    @Column(nullable = false)
    protected LocalDate fecha;

    protected double totalEnvases;

    protected RegistroAuditoria() {
        // JPA
    }

    public RegistroAuditoria(Personal personal, String planta, Contenedor contenedoresAsignados, LocalDate fecha,
            double totalEnvases) {
        this.personal = personal;
        this.planta = planta;
        this.contenedoresAsignados = contenedoresAsignados;
        this.fecha = fecha;
        this.totalEnvases = totalEnvases;
    }

    public Long getId() {
        return id;
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
