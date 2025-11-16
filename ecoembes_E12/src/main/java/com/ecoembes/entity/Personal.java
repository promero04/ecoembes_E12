package com.ecoembes.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Personal {
	private static int contadorId = 0;
	protected int idPersonal; // No está en el diagrama pero considero que es importante
	protected String nombre;
	protected String correo;
	protected String contrasena;
	private LocalDate token; // Tampoco está pero podría ser interesante para la gestión de sesiones

	public Personal(String nombre, String correo, String contrasena) {
		super();
		this.idPersonal = contadorId++;
		this.nombre = nombre;
		this.correo = correo;
		this.contrasena = contrasena;
		this.token = null;
	}

    // He puesto en general más getters y setters de lo que hay puestos en los diagramas por si fueran necesarios en futuros cambios
	public int getIdPersonal() {
		return idPersonal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	
	public LocalDate getToken() {
		return token;
	}
	
	public void setToken() {
		this.token = LocalDate.now();
	}

	public void setTokenNull() {
		this.token = null;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(contrasena, correo, idPersonal, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Personal other = (Personal) obj;
		return Objects.equals(contrasena, other.contrasena)
				&& Objects.equals(correo, other.correo) && idPersonal == other.idPersonal
				&& Objects.equals(nombre, other.nombre);
	}
}