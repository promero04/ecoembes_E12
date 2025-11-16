package com.ecoembes.DTO;

import io.swagger.v3.oas.annotations.media.Schema; // Importación para las anotaciones de Swagger, no se por qué no va

@Schema(description = "Data Transfer Object for Contenedor entity")
    public class ContenedorDTO {
    
	    public enum EstadoEnvase {
		    VERDE, // Hay espacio 
		    NARANJA, // Poco espacio
		    ROJO // Lleno
	    }
	    protected int idContenedor;
	    protected String ubicacion;
	    protected double capInicial;
	    protected EstadoEnvase NEstadoEnvase;
	    protected int numEnvases;
        
        public ContenedorDTO(int idContenedor, String ubicacion, double capInicial, EstadoEnvase nEstadoEnvase,
                int numEnvases) {
            super();
            this.idContenedor = idContenedor;
            this.ubicacion = ubicacion;
            this.capInicial = capInicial;
            NEstadoEnvase = nEstadoEnvase;
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

        public EstadoEnvase getNEstadoEnvase() {
            return NEstadoEnvase;
        }

        public void setNEstadoEnvase(EstadoEnvase nEstadoEnvase) {
            NEstadoEnvase = nEstadoEnvase;
        }

	    public int getNumEnvases() {
		    return numEnvases;
	    }

	    public void setNumEnvases(int numEnvases) {
		    this.numEnvases = numEnvases;
	    }
    }

