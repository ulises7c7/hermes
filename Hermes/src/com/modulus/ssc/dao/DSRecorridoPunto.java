package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Recorrido;
import com.modulus.ssc.model.RecorridoPunto;

public class DSRecorridoPunto extends DSGenerico<RecorridoPunto> {

	public static final String TABLE_RECORRIDOS_PUNTOS = "recorridos_puntos";
	// Columnas Recorridos Puntos
	public static final String COL_ID = "_id";
	public static final String COL_LAT = "lat";
	public static final String COL_LNG = "lng";
	public static final String COL_ORDEN = "orden";
	public static final String COL_RECORRIDO_FK = "id_recorrido";

	private String[] columnas = { COL_ID, COL_LAT, COL_LNG, COL_ORDEN,
			COL_RECORRIDO_FK };

	public static final String CREATE_TABLE_RECORRIDO_PUNTOS = "CREATE TABLE "
			+ TABLE_RECORRIDOS_PUNTOS + "(" + COL_ID
			+ " integer primary key autoincrement, " + COL_LAT
			+ " real not null," + COL_LNG + " real not null," + COL_ORDEN
			+ " integer," + COL_RECORRIDO_FK + " integer not null " + ");";

	public DSRecorridoPunto(Context context) {
		super(context);
	}

	@Override
	public List<RecorridoPunto> getAll() {
		
		return null;
	}

	public List<RecorridoPunto> getByRecorrido(Recorrido recorrido) {
		List<RecorridoPunto> puntosRecorido = new ArrayList<RecorridoPunto>();

		Cursor cursor = db.query(TABLE_RECORRIDOS_PUNTOS, columnas, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RecorridoPunto punto = cursorTo(cursor);
			if (punto.getRecorrido().getId() == recorrido.getId()) {
				puntosRecorido.add(punto);
			}
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
		
		//Recorrido dummy para portar el id_recorrido
		Recorrido recorrido = new Recorrido();
		recorrido.setId(cursor.getLong(4));
		punto.setRecorrido(recorrido);
		
		return punto;
	}

	@Override
	public long create(RecorridoPunto punto) {
		ContentValues values = new ContentValues();
		values.put(COL_LAT, punto.getLat());
		values.put(COL_LNG, punto.getLng());
		values.put(COL_ORDEN, 0);
		values.put(COL_RECORRIDO_FK, punto.getRecorrido().getId());
		return db.insert(TABLE_RECORRIDOS_PUNTOS, null, values);
	}

	@Override
	public RecorridoPunto getById(long id) {
		
		return null;
	}

	@Override
	public void delete(RecorridoPunto entity) {
	

	}

}
