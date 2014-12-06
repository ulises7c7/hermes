package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.model.RecorridoPunto;

public class DSRecorridoPunto extends DSGenerico<RecorridoPunto> {

	public static final String TABLE_RECORRIDOS_PUNTOS = "recorridos_puntos";
	// Columnas Recorridos Puntos
	public static final String COL_ID = "_id";
	public static final String COL_LAT = "lat";
	public static final String COL_LNG = "lng";
	public static final String COL_ORDEN = "orden";
	public static final String COL_RECORRIDO_FK = "id_recorrido";
	
	private String[] columnas = {COL_ID, COL_LAT, COL_LNG, COL_ORDEN, COL_RECORRIDO_FK};

	public static final String CREATE_TABLE_RECORRIDO_PUNTOS = 
			"CREATE TABLE " + TABLE_RECORRIDOS_PUNTOS + "(" + 
					COL_ID + " integer primary key autoincrement, " + 
					COL_LAT + " real not null," + 
					COL_LNG + " real not null," + 
					COL_ORDEN + " integer," + 
					COL_RECORRIDO_FK + " integer not null " + 
					");";

	public DSRecorridoPunto(SSCSQLiteHelper context) {
		super(context);
	}

	@Override
	public List<RecorridoPunto> getAll() {
		return null;
	}
	
	public List<RecorridoPunto> getByRecorrido(long idRecorrido) {
		List<RecorridoPunto> puntosRecorido = new ArrayList<RecorridoPunto>();

		Cursor cursor = db.query(TABLE_RECORRIDOS_PUNTOS, columnas, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RecorridoPunto punto = cursorTo(cursor);
			puntosRecorido.add(punto);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return puntosRecorido;
	}

	
	private RecorridoPunto cursorTo(Cursor cursor) {
		RecorridoPunto punto = new RecorridoPunto();
		punto.setId(cursor.getLong(0));
		punto.setLat(cursor.getDouble(1));
		punto.setLng(cursor.getDouble(2));
		return punto;
	}
	
	
	@Override
	public long create(RecorridoPunto entity) {
		// TODO Auto-generated method stub
		return 0L;
	}

	@Override
	public RecorridoPunto getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(RecorridoPunto entity) {
		// TODO Auto-generated method stub

	}

}
