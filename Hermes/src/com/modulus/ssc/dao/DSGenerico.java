package com.modulus.ssc.dao;


import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DSGenerico<T> {
	protected SQLiteDatabase db;
	protected SSCSQLiteHelper helper;
	protected Context context;

	public abstract List<T> getAll();

	public abstract long create(T entity);

	public abstract T getById(long id);

	public abstract void delete(T entity);

	public DSGenerico(Context context) {
		helper = new SSCSQLiteHelper(context);
	}

	public void open() throws SQLException {
		db = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

}
