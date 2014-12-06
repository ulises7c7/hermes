package com.modulus.ssc.model;

import java.util.List;



public class Recorrido {
	private long id;
	private Linea linea;
	private List<Parada> paradas;
	private List<RecorridoPunto> puntos;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public List<Parada> getParadas() {
		return paradas;
	}

	public void setParadas(List<Parada> paradas) {
		this.paradas = paradas;
	}

	public List<RecorridoPunto> getPuntos() {
		return puntos;
	}

	public void setPuntos(List<RecorridoPunto> puntos) {
		this.puntos = puntos;
	}

}
