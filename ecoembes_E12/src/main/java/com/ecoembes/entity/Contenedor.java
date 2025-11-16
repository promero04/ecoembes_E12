package com.ecoembes.entity;

import java.util.Objects;
import com.ecoembes.entity.EstadoEnvase; 

public class Contenedor {
	
	private static int contadorId = 0;
	protected int idContenedor;
	protected String ubicacion;
	protected double capInicial;
    protected EstadoEnvase estadoEnvase; // Lo mismo que nivelLlenado
	protected int numLlenado;
	
	public Contenedor(String ubicacion, double capInicial, EstadoEnvase estadoEnvase, int numLlenado) {
		super();
		this.idContenedor = contadorId++;
		this.ubicacion = ubicacion;
		this.capInicial = capInicial;
		this.estadoEnvase = EstadoEnvase.VERDE; 
        this.numLlenado = numLlenado;
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
				&& numLlenado == other.numLlenado && idContenedor == other.idContenedor
				&& Objects.equals(ubicacion, other.ubicacion);
	}
}