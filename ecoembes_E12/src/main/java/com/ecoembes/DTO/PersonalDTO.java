package com.ecoembes.DTO;

import io.swagger.v3.oas.annotations.media.Schema; 

@Schema(description = "Data Transfer Object for Empleado entity")
public class PersonalDTO {
	private String correo;
	private String contrasena;

	public PersonalDTO() {
		// Constructor sin argumentos necesario para la deserializacion.
	}

	public PersonalDTO(String correo) {
		this.correo = correo;
	}

	public PersonalDTO(String correo, String contrasena) {
		this.correo = correo;
		this.contrasena = contrasena;
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
}
