package com.modulus.ssc.dao;

import java.util.List;

import android.content.Context;

import com.modulus.ssc.model.Recorrido;

public class DSRecorrido extends DSGenerico<Recorrido> {

	public static final String TABLE_RECORRIDOS = "recorridos";
	// Columnas Recorridos
	public static final String COL_ID = "_id";
	public static final String COL_LINEA_FK = "id_linea";

	public static final String CREATE_TABLE_RECORRIDOS = "CREATE TABLE "
			+ TABLE_RECORRIDOS + "(" + COL_ID
			+ " integer primary key autoincrement, " + COL_LINEA_FK
			+ " integer not null " + ");";

	public DSRecorrido(SSCSQLiteHelper context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Recorrido> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long create(Recorrido entity) {
		// TODO Auto-generated method stub
		return 0L;
	}

	@Override
	public Recorrido getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Recorrido entity) {
		// TODO Auto-generated method stub

	}

}
