package com.example.test.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.test.model.Person;
import com.example.test.model.Person.Parameter;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "database1.db";
	private static final int DB_VERSION = 1;

	private static DBOpenHelper m_instance = null;

	synchronized static public DBOpenHelper getInstance(Context context) {
		if (m_instance == null) {
			m_instance = new DBOpenHelper(context.getApplicationContext());
		}

		return m_instance;
	}

	private DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(getCreateQuery());
	}

	private String getCreateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(PersonStore.TBL_NAME);
		sb.append(" (");
		sb.append("ID integer primary key autoincrement");
		for (Parameter param : Person.Parameter.values()) {
			if (param.equals(Person.Parameter.id)) {
				continue;
			}
			sb.append(" , ");
			sb.append(param.toUpperString());
			sb.append(" ");
			if (param.sql_data_type.equals(String.class)) {
				sb.append("text");
			} else if (param.sql_data_type.equals(int.class)) {
				sb.append("integer");
			}
		}
		sb.append(");");
		// " Test ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , caption TEXT )"
		// sb.append("CREATE TABLE IF NOT EXISTS Test ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , caption TEXT )");
		return sb.toString();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
