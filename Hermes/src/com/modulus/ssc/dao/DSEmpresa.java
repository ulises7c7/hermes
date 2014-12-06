package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.view.ActivityMain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DSEmpresa extends DSGenerico<Empresa> {

	public static final String TABLE_EMPRESAS = "empresas";
	public static final String COL_ID = "_id";
	public static final String COL_NOMBRE = "nombre";

	public static final String CREATE_TABLE_EMPRESAS = "CREATE TABLE "
			+ TABLE_EMPRESAS + "(" + COL_ID
			+ " integer primary key autoincrement, " + COL_NOMBRE
			+ " text not null" + ");";

	private String[] columnas = { COL_ID, COL_NOMBRE };

	public DSEmpresa(SSCSQLiteHelper helper) {
		super(helper);
	}

	@Override
	public List<Empresa> getAll() {
		List<Empresa> empresas = new ArrayList<Empresa>();

		Cursor cursor = db.query(TABLE_EMPRESAS, columnas, null, null, null,
				null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Empresa empresa = cursorTo(cursor);
			empresas.add(empresa);
			cursor.moveToNext();
		}
		cursor.close();
		return empresas;
	}

	private Empresa cursorTo(Cursor cursor) {
		Empresa empresa = new Empresa();
		empresa.setId(cursor.getLong(0));
		empresa.setNombre(cursor.getString(1));
		return empresa;
	}

	@Override
	public long create(Empresa empresa) {
		/*
		String mySql = " SELECT name FROM sqlite_master "
				+ " WHERE type='table'             "
				+ "   AND name LIKE 'PR_%' ";
		Cursor c = db.rawQuery(mySql, null);

		List<String> todoItems = new ArrayList<String>();

		if (c.moveToFirst()) {
			do {
				todoItems.add(c.getString(0));

			} while (c.moveToNext());
		}
		if (todoItems.size() >= 0) {
			for (int i = 0; i < todoItems.size(); i++) {
				Log.i(ActivityMain.TAG_SSC, todoItems.get(i) + "");
			}
		}else{
			Log.i(ActivityMain.TAG_SSC, "No se encontro ningun item");
		}
      */
		
		ContentValues values = new ContentValues();
		values.put(COL_NOMBRE, empresa.getNombre());
		long insertId = db.insert(TABLE_EMPRESAS, null, values);
		/*
		 * Cursor cursor = db.query(TABLE_EMPRESAS, columnas, COL_ID + " = " +
		 * insertId, null, null, null, null); cursor.moveToFirst(); Empresa
		 * newEmpresa = cursorTo(cursor); cursor.close();
		 */
		return insertId;
	}

	@Override
	public void delete(Empresa empresa) {
		long id = empresa.getId();
		System.out.println("Comment deleted with id: " + id);
		db.delete(TABLE_EMPRESAS, COL_ID + " = " + id, null);
	}

	@Override
	public Empresa getById(long id) {
		Cursor cursor = db.query(TABLE_EMPRESAS, new String[] { COL_ID,
				COL_NOMBRE }, COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Empresa empresa = new Empresa();
		empresa.setId(cursor.getLong(0));
		empresa.setNombre(cursor.getString(1));
		return empresa;
	}

}
