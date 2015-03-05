package com.modulus.ssc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SSCSQLiteHelper extends SQLiteOpenHelper {
	// TODO: cambiar valor a "ssc.db" cuando se haga el release
	private static final String DATABASE_NAME = "/storage/emulated/0/Download/Documentos/ssc.db";
	private static final int DATABASE_VERSION = 21;

	public SSCSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DSEmpresa.CREATE_TABLE_EMPRESAS);
		database.execSQL(DSLinea.CREATE_TABLE_LINEAS);
		database.execSQL(DSParada.CREATE_TABLE_PARADAS);
		database.execSQL(DSRecorridoPunto.CREATE_TABLE_RECORRIDO_PUNTOS);
		database.execSQL(DSRecorrido.CREATE_TABLE_RECORRIDOS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SSCSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DSEmpresa.TABLE_EMPRESAS);
		onCreate(db);
	}

}
