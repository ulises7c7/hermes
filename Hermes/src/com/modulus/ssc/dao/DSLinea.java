package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.model.Recorrido;
import com.modulus.ssc.view.ActivityMain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DSLinea extends DSGenerico<Linea> {

	public static final String TABLE_LINEAS = "lineas";

	public static final String COL_ID = "_id"; // 0
	public static final String COL_NUMERO = "numero"; // 1
	public static final String COL_RAMAL = "ramal"; // 2
	public static final String COL_EMPRESA_FK = "id_empresa"; // 3

	public static final String CREATE_TABLE_LINEAS = "CREATE TABLE "
			+ TABLE_LINEAS + "(" + COL_ID
			+ " integer primary key autoincrement, " + COL_NUMERO
			+ " text not null," + COL_RAMAL + " text," + COL_EMPRESA_FK
			+ " integer not null " + ");";

	private String[] allColumns = { COL_ID, COL_NUMERO, COL_RAMAL,
			COL_EMPRESA_FK };

	
	public DSLinea(Context context) {
		super(context);

	}

	
	public List<Linea> getAll() {
		List<Linea> lineas = new ArrayList<Linea>();
		Cursor cursor = db.query(TABLE_LINEAS, allColumns, null, null, null,
				null, null);
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
		
		DSRecorrido dsRecorrido = new DSRecorrido(context);
		dsRecorrido.open();
		List<Recorrido> recorridos =  dsRecorrido.getByLinea(linea);
		//Se prevee permitir varios recorridos por linea
		linea.setRecorrido(recorridos.get(0));

		return linea;
	}

	public long create(Linea linea) {
		ContentValues values = new ContentValues();
		values.put(COL_NUMERO, linea.getNumero());
		values.put(COL_RAMAL, linea.getRamal());
		values.put(COL_EMPRESA_FK, linea.getEmpresa().getId());
		return db.insert(TABLE_LINEAS, null, values);
	}

	private Linea cursorTo(Cursor cursor) { 
		Linea linea = new Linea();
		linea.setId(cursor.getLong(0));
		linea.setNumero(cursor.getString(1));
		linea.setRamal(cursor.getString(2));
		long idEmpresa = cursor.getLong(3);
		// Buscar la empresa correspondiente
		DSEmpresa dsEmpresa = new DSEmpresa(context);
		dsEmpresa.open();
		Empresa empresa = dsEmpresa.getById(idEmpresa);
		linea.setEmpresa(empresa);
		dsEmpresa.close();
		
		DSRecorrido dsRecorrido =  new DSRecorrido(context);
		dsRecorrido.open();
		List<Recorrido> recorridos = dsRecorrido.getByLinea(linea);
		if (recorridos != null && recorridos.size() >0){
			linea.setRecorrido(recorridos.get(0));
		}
		dsRecorrido.close();
		
		return linea;
	}

	@Override
	public Linea getById(long id) {
		String[] params = { String.valueOf(id) };

		Cursor cursor = db.query(TABLE_LINEAS, allColumns, COL_ID + "=?",
				params, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		return cursorTo(cursor);

	}

	@Override
	public void delete(Linea linea) {
		long id = linea.getId();

		db.delete(TABLE_LINEAS, COL_ID + " = " + id, null);
	}

}