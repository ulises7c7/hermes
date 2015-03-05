package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import com.modulus.ssc.model.Empresa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DSEmpresa extends DSGenerico<Empresa> {
	public static final String TABLE_EMPRESAS = "empresas";
	public static final String COL_ID = "_id";
	public static final String COL_NOMBRE = "nombre";
	public static final String CREATE_TABLE_EMPRESAS = "CREATE TABLE "
			+ TABLE_EMPRESAS + "(" + COL_ID
			+ " integer primary key autoincrement, " + COL_NOMBRE
			+ " text not null" + ");";
	private String[] allColumns = { COL_ID, COL_NOMBRE };

	public DSEmpresa(Context context) {
		super(context);
	}

	public long create(Empresa empresa) {
		ContentValues values = new ContentValues();
		values.put(COL_NOMBRE, empresa.getNombre());
		return db.insert(TABLE_EMPRESAS, null, values);
	}

	public void delete(Empresa empresa) {
		long id = empresa.getId();
		System.out.println("Empresa deleted with id: " + id);
		db.delete(TABLE_EMPRESAS, COL_ID + " = " + id, null);
	}

	public List<Empresa> getAll() {
		List<Empresa> empresas = new ArrayList<Empresa>();
		Cursor cursor = db.query(TABLE_EMPRESAS, allColumns, null, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Empresa empresa = cursorTo(cursor);
			empresas.add(empresa);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
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
	public Empresa getById(long id) {
		Cursor cursor = db.query(TABLE_EMPRESAS, new String[] { COL_ID,
				COL_NOMBRE }, COL_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		return cursorTo(cursor);
	}

}
