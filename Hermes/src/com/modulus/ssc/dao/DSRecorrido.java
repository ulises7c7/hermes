package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.SyncStateContract.Columns;

import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.model.Parada;
import com.modulus.ssc.model.Recorrido;
import com.modulus.ssc.model.RecorridoPunto;

public class DSRecorrido extends DSGenerico<Recorrido> {

	public static final String TABLE_RECORRIDOS = "recorridos";
	// Columnas Recorridos
	public static final String COL_ID = "_id";
	public static final String COL_LINEA_FK = "id_linea";

	private static final String[] allColumns = { COL_ID, COL_LINEA_FK };

	public static final String CREATE_TABLE_RECORRIDOS = "CREATE TABLE "
			+ TABLE_RECORRIDOS + "(" + COL_ID
			+ " integer primary key autoincrement, " + COL_LINEA_FK
			+ " integer not null " + ");";

	public DSRecorrido(Context context) {
		super(context);
	}

	@Override
	public List<Recorrido> getAll() {
		return null;
	}

	@Override
	public long create(Recorrido recorrido) {
		ContentValues values = new ContentValues();
		values.put(COL_LINEA_FK, recorrido.getLinea().getId());
		return db.insert(TABLE_RECORRIDOS, null, values);
	}

	@Override
	public Recorrido getById(long id) {
		
		return null;
	}

	@Override
	public void delete(Recorrido entity) {
		
	}

	public List<Recorrido> getByLinea(Linea linea) {
		
	
		List<Recorrido> recorridos = new ArrayList<Recorrido>();
		Cursor cursor = db.query(TABLE_RECORRIDOS, allColumns, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Recorrido recorrido = cursorTo(cursor);
			if (recorrido.getLinea().getId() == linea.getId()) {
				linea.setRecorrido(recorrido);
				recorridos.add(recorrido);
			}
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return recorridos;
	}

	private Recorrido cursorTo(Cursor cursor) {
		Recorrido recorrido = new Recorrido();
		recorrido.setId(cursor.getLong(0));

		Linea lineaDummy = new Linea();
		lineaDummy.setId(cursor.getLong(0));

		recorrido.setLinea(lineaDummy);
		
		DSParada dsParada = new DSParada(context);
		dsParada.open();
		List<Parada>paradas = dsParada.getByRecorrido(recorrido);
		recorrido.setParadas(paradas);
		dsParada.close();
		
		DSRecorridoPunto dsRecorridoPunto = new DSRecorridoPunto(context);
		dsRecorridoPunto.open();
		List<RecorridoPunto> puntos = dsRecorridoPunto.getByRecorrido(recorrido);
		recorrido.setPuntos(puntos);
		dsParada.close();
		
		

		return recorrido;
	}

}
