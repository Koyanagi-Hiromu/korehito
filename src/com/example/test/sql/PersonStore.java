package com.example.test.sql;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;

import com.example.test.model.Person;

public class PersonStore {
	public final static String ex_key = "UserList";

	private DBOpenHelper m_helper;

	@SuppressWarnings("unchecked")
	public static ArrayList<Person> getAllInList(ActionBarActivity context) {
		Serializable obj = context.getIntent().getSerializableExtra(
				PersonStore.ex_key);
		if (obj != null) {
			try {
				return (ArrayList<Person>) obj;
			} catch (Exception e) {
			}
		}
		ArrayList<Person> personList = new ArrayList<Person>();
		PersonStore store;
		store = new PersonStore(context);
		for (Person p : store.loadAll()) {
			personList.add(p);
		}
		store.close();
		return personList;

	}

	private SQLiteDatabase m_db;
	public static final String TBL_NAME = "PERSON";

	/**
	 * Don't forget to call "close" method.
	 * 
	 * @param context
	 */
	public PersonStore(Context context) {
		m_helper = DBOpenHelper.getInstance(context);
		if (m_helper != null) {
			m_db = m_helper.getWritableDatabase();
		} else {
			m_db = null;
		}
	}

	public void delete(Person p) {
		m_db.delete(TBL_NAME, Person.Parameter.id.toUpperString() + "=?",
				new String[] { Integer.toString(p.get_id()) });
	}

	public void add(Person p) {
		ContentValues val = new ContentValues();
		p.saveData(val);
		long id = m_db.insert(TBL_NAME, null, val);
		p.set_id((int) id);
	}

	public static void updateOpenClose(Person hero, Context context) {
		PersonStore store = new PersonStore(context);
		store.update(hero);
		store.close();
	}

	public void update(Person p) {
		ContentValues val = new ContentValues();
		p.saveData(val);
		m_db.update(TBL_NAME, val, Person.Parameter.id.toUpperString() + "=?",
				new String[] { Integer.toString(p.get_id()) });
	}

	public void close() {
		m_db.close();
	}

	public Person[] loadAll() {
		int i;
		Cursor c;
		Person[] entries;

		if (m_db == null) {
			return null;
		}

		c = m_db.query(TBL_NAME, Person.Parameter.valuesToStrings(), null,
				null, null, null, null);

		int numRows = c.getCount();

		c.moveToFirst();

		entries = new Person[numRows];
		for (i = 0; i < numRows; i++) {
			entries[i] = new Person(c);
			c.moveToNext();
		}
		c.close();

		return entries;
	}

	public int getEntriedNumber() {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ");
		sql.append(TBL_NAME);
		Cursor c = m_db.rawQuery(sql.toString(), null);
		c.moveToLast();
		int count = c.getCount();
		c.close();
		return count;
	}

}
