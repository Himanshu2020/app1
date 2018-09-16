
package com.musipo.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.musipo.dao.tables.ChatRoomTbl;
import com.musipo.dao.tables.FcmTbl;
import com.musipo.dao.tables.LastSeenTbl;
import com.musipo.dao.tables.MessageTbl;
import com.musipo.dao.tables.PlayingStatusTbl;
import com.musipo.dao.tables.StatusTbl;
import com.musipo.dao.tables.UserTable;
import com.musipo.util.Utils;


public class DBAdapter {

	//public static final String DATABASE_NAME = "musipo_db_dev1234"+ Utils.getOtpNumber(5);
	public static final String DATABASE_NAME = "temp_db";
	private static final int DATABASE_VERSION = 1;


	private final Context context;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private static DBAdapter dbAdapter;

	public static DBAdapter getInstance() {
		return dbAdapter;
	}

	public static DBAdapter initialize(Context context) {
		if (dbAdapter == null) {
			dbAdapter = new DBAdapter(context);
		}
		return dbAdapter;
	}

	public DBAdapter(Context ctx) {
		this.context = ctx;
		try {
			dbHelper = new DatabaseHelper(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		open();

	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			getWritableDatabase();
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			try {
				createTables(db);


			} catch (SQLException e) {
				e.printStackTrace();

			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//			reCreateTables(db);
//			db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
//			db.execSQL("DROP TABLE IF EXISTS " + ServiceTable.TABLE_NAME);
//			onCreate(db);

		}


		/**
		 * Instead of deleting large no of records on user logout, this method drop the tables and recreates it.
		 * @param db - database instance
		 *
		 */
		private void reCreateTables(SQLiteDatabase db) {


		//	db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
	//		createTables(db);

		}

		/**
		 * Create tables.
		 * @param db - database instance
		 *
		 */
		private void createTables(SQLiteDatabase db) {
			db.execSQL(ChatRoomTbl.STATEMENT_CREATE_TABLE);
			db.execSQL(MessageTbl.STATEMENT_CREATE_TABLE);
			db.execSQL(FcmTbl.STATEMENT_CREATE_TABLE);
			db.execSQL(LastSeenTbl.STATEMENT_CREATE_TABLE);
			db.execSQL(UserTable.STATEMENT_CREATE_TABLE);
			db.execSQL(StatusTbl.STATEMENT_CREATE_TABLE);
			db.execSQL(PlayingStatusTbl.STATEMENT_CREATE_TABLE);
		}

	}

	// ---opens the database---
	public SQLiteDatabase open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return db;
	}

	// ---closes the database---
	public void close() {
		dbHelper.close();
	}

	// ---reCreateTables the database---
	public void reCreateTables() {
		dbHelper.reCreateTables(db);
	}


}
