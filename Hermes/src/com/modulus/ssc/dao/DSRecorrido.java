package com.modulus.ssc.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.SyncStateContract.Columns;

import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.model.Linea;
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

	public List<Recorrido> getByLinea(long id) {
		List<Recorrido> recorridos = new ArrayList<Recorrido>();
		Cursor cursor = db.query(TABLE_RECORRIDOS, allColumns, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Recorrido recorrido = cursorTo(cursor);
			if (id == recorrido.getLinea().getId()) {
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

		long idLinea = cursor.getLong(0);
		DSLinea dsLinea = new DSLinea(context);
		dsLinea.open();
		Linea linea = dsLinea.getById(idLinea);
		dsLinea.close();

		recorrido.setLinea(linea);

		return recorrido;
	}

}
