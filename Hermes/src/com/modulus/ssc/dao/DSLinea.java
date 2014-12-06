package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.view.ActivityMain;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;


public class DSLinea extends DSGenerico<Linea> {


	public static final String TABLE_LINEAS = "lineas";
	public static final String COL_ID = "_id";
	public static final String COL_NUMERO = "numero";
	public static final String COL_RAMAL = "ramal";
	public static final String COL_EMPRESA_FK = "id_empresa";
	
	public static final String CREATE_TABLE_LINEAS = "CREATE TABLE " + TABLE_LINEAS + "(" + 
			COL_ID + " integer primary key autoincrement, " + 
			COL_NUMERO + " text not null," + 
			COL_RAMAL + " text,"	+ 
			COL_EMPRESA_FK + " integer not null " + 
			");";
	

	private String[] columnas = { COL_ID,
			COL_NUMERO,
			COL_RAMAL,
			COL_EMPRESA_FK };
	
	public DSLinea(SSCSQLiteHelper context) {
		super(context);
		Log.i(ActivityMain.TAG_SSC, "Llamada al constructor del datasource");
	}

	public List<Linea> getAll() {
		List<Linea> lineas = new ArrayList<Linea>();

		Cursor cursor = db.query(TABLE_LINEAS, columnas, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Linea linea = cursorToLinea(cursor);
			lineas.add(linea);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return lineas;
	}

	private Linea cursorToLinea(Cursor cursor) {
		Linea linea = new Linea();
		linea.setId(cursor.getLong(0));
		linea.setNumero(cursor.getString(1));
		linea.setRamal(cursor.getString(2));
		long idEmpresa = cursor.getLong(3);
		DSEmpresa dsEmpresa = new DSEmpresa(context);
		dsEmpresa.open();
		Empresa empresa = dsEmpresa.getById(idEmpresa);
		linea.setEmpresa(empresa);
		dsEmpresa.close();
		
		
		
		return linea;
	}

	@Override
	public long create(Linea entity) {
		// TODO Auto-generated method stub
		return 0L;
	}

	@Override
	public Linea getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Linea entity) {
		// TODO Auto-generated method stub
		
	}
	
	
}