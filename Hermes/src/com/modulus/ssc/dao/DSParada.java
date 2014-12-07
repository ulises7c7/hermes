package com.modulus.ssc.dao;

import java.util.List;

import android.content.Context;

import com.modulus.ssc.model.Parada;

public class DSParada extends DSGenerico<Parada> {


	public static final String TABLE_PARADAS = "paradas";

	// Columnas Paradas
	public static final String COL_ID = "_id";
	public static final String COL_LAT = "lat";
	public static final String COL_LNG = "lng";
	public static final String COL_DESCRIPCION = "descripcion";
	public static final String COL_RECORRIDO_FK = "id_recorrido";
	
	public static final String CREATE_TABLE_PARADAS = "CREATE TABLE " + TABLE_PARADAS + "(" + 
			COL_ID + " integer primary key autoincrement, " + 
			COL_LAT + " real not null," + 
			COL_LNG + " real not null," + 
			COL_DESCRIPCION + " text ," + 
			COL_RECORRIDO_FK + " integer not null " + ");";

	public DSParada(Context context, SSCSQLiteHelper helper) {
		super(context, helper);
	}
	
	@Override
	public List<Parada> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long create(Parada entity) {
		// TODO Auto-generated method stub
		return 0L;
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

}
