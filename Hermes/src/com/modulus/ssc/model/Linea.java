package com.modulus.ssc.model;

import java.util.List;

import android.location.Location;

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
		double distancia = 0d;

		if (lineas != null && lineas.size() > 0) {

			for (int i = 0; i < lineas.size(); i++) {
				Linea currentL = lineas.get(i);

				Parada pOrigen = currentL.recomendarParada(origen);
				Parada pDestino = currentL.recomendarParada(destino);

				boolean cond = pOrigen != null && pDestino != null;

				if (cond) {
					double dOrigen = getDistance(new LatLng(pOrigen.getLat(),
							pOrigen.getLng()), origen);
					double dDestino = getDistance(new LatLng(pDestino.getLat(),
							pDestino.getLng()), origen);

					if (lineaElegida == null || distancia > dOrigen + dDestino) {
						lineaElegida = lineas.get(i);
						distancia = dOrigen + dDestino;
					}
				}

			}
		}
		return lineaElegida;
	}

	public Parada recomendarParada(LatLng ubicacion) {

		Parada theChosenOne = null;
		double theChosenOneDistance = 0d;
		boolean cond = this.getRecorrido() != null
				&& this.getRecorrido().getParadas() != null
				&& this.getRecorrido().getParadas().size() > 0;
		if (cond) {

			List<Parada> paradas = this.getRecorrido().getParadas();
			for (int i = 0; i < paradas.size(); i++) {
				double dParada = getDistance(new LatLng(
						paradas.get(i).getLat(), paradas.get(i).getLng()),
						ubicacion);

				if (theChosenOne == null || theChosenOneDistance > dParada) {
					theChosenOneDistance = dParada;
					theChosenOne = paradas.get(i);
				}
			}
		}
		return theChosenOne;
	}

	private static double getDistance(LatLng LatLng1, LatLng LatLng2) {
		double distance = 0;
		Location locationA = new Location("A");
		locationA.setLatitude(LatLng1.latitude);
		locationA.setLongitude(LatLng1.longitude);
		Location locationB = new Location("B");
		locationB.setLatitude(LatLng2.latitude);
		locationB.setLongitude(LatLng2.longitude);
		distance = locationA.distanceTo(locationB);
		return distance;

	}

}
