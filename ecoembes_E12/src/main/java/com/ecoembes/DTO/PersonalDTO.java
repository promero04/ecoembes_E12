package com.ecoembes.DTO;

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
