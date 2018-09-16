package com.musipo.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public abstract class Dao<T> {

	protected SQLiteDatabase sqLiteDb = null;
	protected DBAdapter db = DBAdapter.getInstance();
	private String tableName = null;
	protected boolean bulkSave = false;
	protected String alias;
	protected String recordStatusCondition;
	protected String recordStatusConditionOR;

	public final String TAG = Dao.class.getSimpleName();

	public Dao(String tableName) {

		if (sqLiteDb == null) {
			if(db == null){
				db = DBAdapter.getInstance();
			}

			sqLiteDb = db.open();
		}
		this.tableName = tableName ;

	}

	public Dao(String tableName, String alias) {

		if (sqLiteDb == null) {
			if(db == null){
				db = DBAdapter.getInstance();
			}
			sqLiteDb = db.open();
		}
		this.tableName = tableName ;

	}

	/**
	 * @param t type to be converted to ContentValue
	 * @return ContentValues from data of type <T> for insertion in database.
	 */
	public abstract ContentValues getContentValues(T t) ;


	/**
	 * @param cursor  records fetch from database
	 * @return object of type <T> set from  @param cursor
	 */
	public abstract T cursorToModel(Cursor cursor);


	/**
	 * @param cursor  records fetch from database
	 * @return object of type <T> set from  @param cursor
	 */
	public abstract List<T> cursorToModelList(Cursor cursor);


	/**
	  * open db if it is close
	  *
	  */
	 public void openDB() {

	  if(!sqLiteDb.isOpen())
	   sqLiteDb = db.open();
	 }

	 /**
	  * close db if it is open
	  *
	  */
	 public void closeDB() {

	  if(sqLiteDb.isOpen())
		  db.close();
	 }




	/**
	 * @param len
	 * @return
	 */
	final public String makePlaceHolders(int len) {
		if (len < 1) {
			// It will lead to an invalid query anyway ..
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(len * 2 - 1);
			sb.append("?");
			for (int i = 1; i < len; i++) {
				sb.append(",?");
			}
			return sb.toString();
		}
	}

	final public String makePlaceHolders(int len, List<String> elementList) {
		if (len < 1) {
			// It will lead to an invalid query anyway ..
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(len * 2 - 1);
			if (elementList == null) {
				sb.append("?");
				for (int i = 1; i < len; i++) {

					sb.append(",?");
				}
			} else {
				sb.append("'" + elementList.get(0));
				for (int i = 1; i < len; i++) {

					sb.append("','" + elementList.get(i));
				}
				sb.append("'");
			}

			return sb.toString();
		}
	}


	final public String get(Cursor cursor , String columnName){
		return cursor.getString(cursor.getColumnIndex(columnName));
	}

}
