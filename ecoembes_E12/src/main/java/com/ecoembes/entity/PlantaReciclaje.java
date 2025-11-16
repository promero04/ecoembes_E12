package com.ecoembes.entity;

import java.util.Objects;

public class PlantaReciclaje {
	protected static int contadorId = 0;
	protected int id;
	protected String nombre;

	public PlantaReciclaje(String nombre) {
		super();
		this.id = contadorId++;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlantaReciclaje other = (PlantaReciclaje) obj;
		return id == other.id
				&& Objects.equals(nombre, other.nombre);
	}
}