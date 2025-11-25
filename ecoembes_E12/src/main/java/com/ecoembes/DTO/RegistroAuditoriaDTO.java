package com.ecoembes.DTO;

import java.time.LocalDate;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for RegistroAuditoria entity")
public class RegistroAuditoriaDTO {

    private PersonalDTO personal;
    private String planta;
    private ContenedorDTO contenedorAsignado;
    private LocalDate fecha;
    private double totalEnvases;

    public RegistroAuditoriaDTO() {
        // Constructor sin argumentos necesario para la deserializacion.
    }

    public RegistroAuditoriaDTO(PersonalDTO personal, String planta, ContenedorDTO contenedorAsignado, LocalDate fecha,
            double totalEnvases) {
        this.personal = personal;
        this.planta = planta;
        this.contenedorAsignado = contenedorAsignado;
        this.fecha = fecha;
        this.totalEnvases = totalEnvases;
    }

    public PersonalDTO getPersonal() {
        return personal;
    }

    public void setPersonal(PersonalDTO personal) {
        this.personal = personal;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public ContenedorDTO getContenedorAsignado() {
        return contenedorAsignado;
    }

    public void setContenedorAsignado(ContenedorDTO contenedorAsignado) {
        this.contenedorAsignado = contenedorAsignado;
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

    @Override
    public int hashCode() {
        return Objects.hash(contenedorAsignado, fecha, personal, planta, totalEnvases);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RegistroAuditoriaDTO other = (RegistroAuditoriaDTO) obj;
        return Double.doubleToLongBits(totalEnvases) == Double.doubleToLongBits(other.totalEnvases)
                && Objects.equals(contenedorAsignado, other.contenedorAsignado)
                && Objects.equals(fecha, other.fecha)
                && Objects.equals(personal, other.personal)
                && Objects.equals(planta, other.planta);
    }
}
