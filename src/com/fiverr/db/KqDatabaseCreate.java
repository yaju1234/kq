package com.fiverr.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class KqDatabaseCreate {

	public static final String TABLE_TEST_CREATE = "CREATE TABLE "
			+ DatabaseConstant.TABLE_NAME + " (" + DatabaseConstant.ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + DatabaseConstant.POST_ID
			+ " TEXT)";

	public static void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		Log.i("Table created", "Table created");
		try {
			db.execSQL(TABLE_TEST_CREATE);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(DatabaseConstant.class.getName(), "Upgrade database from version "
				+ oldVersion + " to " + newVersion + "");
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstant.TABLE_NAME);
		onCreate(db);
	}
}
