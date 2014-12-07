package com.modulus.ssc.dao;

import com.modulus.ssc.view.ActivityMain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SSCSQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ssc.db";
	private static final int DATABASE_VERSION = 7;

	// Database creation sql statements

	private static final String DATABASE_CREATE = DSParada.CREATE_TABLE_PARADAS
			+ DSLinea.CREATE_TABLE_LINEAS + DSEmpresa.CREATE_TABLE_EMPRESAS
			+ DSRecorrido.CREATE_TABLE_RECORRIDOS
			+ DSRecorridoPunto.CREATE_TABLE_RECORRIDO_PUNTOS;

	public SSCSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.i(ActivityMain.TAG_SSC, "Creando la base de datos");
		Log.i(ActivityMain.TAG_SSC, DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SSCSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DSEmpresa.TABLE_EMPRESAS);
		db.execSQL("DROP TABLE IF EXISTS " + DSLinea.TABLE_LINEAS);
		db.execSQL("DROP TABLE IF EXISTS " + DSParada.TABLE_PARADAS);
		db.execSQL("DROP TABLE IF EXISTS " + DSRecorrido.TABLE_RECORRIDOS);
		db.execSQL("DROP TABLE IF EXISTS " + DSRecorridoPunto.TABLE_RECORRIDOS_PUNTOS);
		onCreate(db);
	}

}
