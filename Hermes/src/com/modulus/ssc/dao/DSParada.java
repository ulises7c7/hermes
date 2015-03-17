package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.modulus.ssc.model.Parada;
import com.modulus.ssc.model.Recorrido;
import com.modulus.ssc.model.RecorridoPunto;

public class DSParada extends DSGenerico<Parada> {

	public static final String TABLE_PARADAS = "paradas";

	// Columnas Paradas
	public static final String COL_ID = "_id";
	public static final String COL_LAT = "lat";
	public static final String COL_LNG = "lng";
	public static final String COL_DESCRIPCION = "descripcion";
	public static final String COL_RECORRIDO_FK = "id_recorrido";

	private String[] columnas = { COL_ID, COL_LAT, COL_LNG, COL_DESCRIPCION,
			COL_RECORRIDO_FK };

	public static final String CREATE_TABLE_PARADAS = "CREATE TABLE "
			+ TABLE_PARADAS + "(" + COL_ID
			+ " integer primary key autoincrement, " + COL_LAT
			+ " real not null," + COL_LNG + " real not null," + COL_DESCRIPCION
			+ " text ," + COL_RECORRIDO_FK + " integer not null " + ");";

	public DSParada(Context context) {
		super(context);
	}

	@Override
	public List<Parada> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long create(Parada parada) {
		ContentValues values = new ContentValues();
		values.put(COL_LAT, parada.getLat());
		values.put(COL_LNG, parada.getLng());
		values.put(COL_RECORRIDO_FK, parada.getRecorrido().getId());
		return db.insert(TABLE_PARADAS, null, values);
	}

	@Override
	public Parada getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Parada entity) {
		// TODO Auto-generated method stub
	}

	private Parada cursorTo(Cursor cursor) {
		Parada parada = new Parada();
		parada.setId(cursor.getLong(0));
		parada.setLat(cursor.getDouble(1));
		parada.setLng(cursor.getDouble(2));

		Recorrido recorridoDummy = new Recorrido();
		recorridoDummy.setId(cursor.getLong(4));
		parada.setRecorrido(recorridoDummy);
		return parada;
	}

	public List<Parada> getByRecorrido(Recorrido recorrido) {

		List<Parada> paradas = new ArrayList<Parada>();

		Cursor cursor = db.query(TABLE_PARADAS, columnas, null, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Parada parada = cursorTo(cursor);
			if (parada.getRecorrido().getId() == recorrido.getId()) {
				parada.setRecorrido(recorrido);
				paradas.add(parada);
			}
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return paradas;

	}

}
