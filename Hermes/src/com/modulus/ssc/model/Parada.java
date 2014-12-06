package com.modulus.ssc.model;

import com.google.android.gms.maps.model.LatLng;

public class Parada {
	private long id;
	private LatLng ubicacion;
	private String descripcion;

	public LatLng getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(LatLng ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
