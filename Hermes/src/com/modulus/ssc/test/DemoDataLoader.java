package com.modulus.ssc.test;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.modulus.ssc.dao.DSEmpresa;
import com.modulus.ssc.dao.DSLinea;
import com.modulus.ssc.dao.DSRecorrido;
import com.modulus.ssc.dao.DSRecorridoPunto;
import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.model.Recorrido;
import com.modulus.ssc.model.RecorridoPunto;
import com.modulus.ssc.view.ActivityMain;

public class DemoDataLoader {

	private List<Empresa> empresas;
	private List<Linea> lineas = new ArrayList<Linea>();
	private List<Recorrido> recorridos = new ArrayList<Recorrido>();
	DSEmpresa dsEmpresa;
	DSLinea dsLinea;
	DSRecorrido dsRecorrido;
	DSRecorridoPunto dsRecorridoPunto;

	public DemoDataLoader(Context context) {
		dsEmpresa = new DSEmpresa(context);
		dsLinea = new DSLinea(context);
		dsRecorrido = new DSRecorrido(context);
		dsRecorridoPunto = new DSRecorridoPunto(context);
	}

	private void crearEmpresas() {
		String[] nombres = { "ERSA Urbano S.A.", "Empresa 1º de Enero S.R.L.",
				"Gran Resistencia S.R.L." };
		empresas = new ArrayList<Empresa>();
		dsEmpresa.open();
		for (int i = 0; i < nombres.length; i++) {
			Empresa empresa = new Empresa();
			empresa.setNombre(nombres[i]);
			long id = dsEmpresa.create(empresa);
			empresa.setId(id);
			empresas.add(empresa);
			Log.v(ActivityMain.TAG_SSC,
					"Insertando empresa " + empresa.getNombre() + "con el id "
							+ empresa.getId());
		}
		dsEmpresa.close();
	}

	private void crearLineas() {

		Linea linea = new Linea();
		linea.setNumero("3");
		linea.setEmpresa(empresas.get(0));
		linea.setRamal("A");

		dsLinea.open();
		long id = dsLinea.create(linea);
		linea.setId(id);
		
		lineas.add(linea);

		Log.v(ActivityMain.TAG_SSC, "Insertando linea " + linea.getNumero()
				+ " " + linea.getRamal() + " con el id " + id);

	}

	private void crearRecorridos() {
		Recorrido recorrido = new Recorrido();
		recorrido.setLinea(lineas.get(0));
		recorridos.add(recorrido);
		dsRecorrido.open();
		long idRecorrido = dsRecorrido.create(recorrido);
		recorrido.setId(idRecorrido);

		List<RecorridoPunto> puntosRecorrido = new ArrayList<RecorridoPunto>();

		dsRecorridoPunto.open();

		Float[][] puntos = { new Float[] { -27.4559121f, -58.98296710000001f },
				new Float[] { -27.448809700000005f, -58.97528530000001f },
				new Float[] { -27.4496094f, -58.9743948f },
				new Float[] { -27.445563f, -58.9699531f },
				new Float[] { -27.442382799999997f, -58.97347210000001f },
				new Float[] { -27.4439634f, -58.97519950000001f },
				new Float[] { -27.442363800000003f, -58.97696969999999f },
				new Float[] { -27.449619f, -58.9847803f },
				new Float[] { -27.4511899f, -58.9830101f },
				new Float[] { -27.4535606f, -58.9856386f },
				new Float[] { -27.4559121f, -58.98296710000001f } };

		for (int i = 0; i < puntos.length; i++) {
			RecorridoPunto punto = new RecorridoPunto();
			punto.setLat(puntos[i][0]);
			punto.setLng(puntos[i][1]);
			punto.setRecorrido(recorrido);
			long idPunto = dsRecorridoPunto.create(punto);

			punto.setId(idPunto);

		}
		dsRecorrido.close();
		dsRecorridoPunto.close();
	}

	public void load() {
		crearEmpresas();
		crearLineas();
		 crearRecorridos();

	}

}
