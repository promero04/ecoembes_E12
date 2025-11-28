package com.contsocket.model;

import java.time.LocalDate;

public class CapacidadSocket {
    private LocalDate fecha;
    private double capacidadTon;

    public CapacidadSocket(LocalDate fecha, double capacidadTon) {
        this.fecha = fecha;
        this.capacidadTon = capacidadTon;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getCapacidadTon() {
        return capacidadTon;
    }

    public void setCapacidadTon(double capacidadTon) {
        this.capacidadTon = capacidadTon;
    }
}
