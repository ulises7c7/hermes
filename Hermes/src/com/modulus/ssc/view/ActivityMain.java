package com.modulus.ssc.view;

import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.modulus.ssc.R;
import com.modulus.ssc.dao.DSEmpresa;
import com.modulus.ssc.dao.DSLinea;
import com.modulus.ssc.dao.SSCSQLiteHelper;
import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.model.Recorrido;
import com.modulus.ssc.model.RecorridoPunto;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ActivityMain extends Activity {
	public static final String TAG_SSC = "Modulus";

	PolygonOptions rectOptions = new PolygonOptions().add(new LatLng(37.35,
			-122.0), new LatLng(37.45, -122.0), new LatLng(37.45, -122.2),
			new LatLng(37.35, -122.2), new LatLng(37.35, -122.0));

	private GoogleMap myMap;
	private Linea linea;
	DSLinea dsLinea;
	DSEmpresa dsEmpresa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		Polygon polygon = myMap.addPolygon(rectOptions);
		myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		Log.i(TAG_SSC, "Se va a inicializar");
		inicializar();

	}

	private void inicializar() {
		//dsLinea = new DSLinea(this);
		SSCSQLiteHelper helper = new SSCSQLiteHelper(this);
		dsEmpresa = new DSEmpresa(helper);
		dsEmpresa.open();
		
		Empresa empresa = new Empresa();
		empresa.setNombre("Ataco");
		long id = dsEmpresa.create(empresa);
		empresa.setId(id);
		Log.e(TAG_SSC, "Se ha insertado la empresa con el id "+ id);
		
		List<Empresa> empresas = dsEmpresa.getAll();
		Log.e(TAG_SSC, "Se han encontrado " + empresas.size()+" empresas en la base de datos-");
		
		

		
		dsEmpresa.close();
		/*
		Recorrido recorrido = new Recorrido();
		
		
		RecorridoPunto recorridoPunto = new RecorridoPunto();

		linea = new Linea();
		linea.setNumero("3");
		linea.setRamal("A");
		linea.setEmpresa(empresa);
		linea = dsLinea.create(linea);
		*/
	}
}
