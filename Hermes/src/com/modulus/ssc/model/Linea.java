package com.modulus.ssc.model;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class Linea {
	private long id;
	private String numero;
	private String ramal;
	private Empresa empresa;
	private Recorrido recorrido;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Recorrido getRecorrido() {
		return recorrido;
	}

	public void setRecorrido(Recorrido recorrido) {
		this.recorrido = recorrido;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return numero;
	}

	public static Linea recomendarLinea(List<Linea> lineas, LatLng origen,
			LatLng destino) {

		Linea lineaElegida = null;

		if (lineas != null && lineas.size() > 0) {

			for (int i = 0; i < lineas.size(); i++) {

			}
		}
		return lineaElegida;
	}

	public Parada recomendarParada(LatLng ubicacion) {
		return null;
	}

}
