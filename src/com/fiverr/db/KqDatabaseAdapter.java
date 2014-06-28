package com.fiverr.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class KqDatabaseAdapter {
	public static Context sContext;
	public static SQLiteDatabase sDb;
	public static KQDatabaseHelper mHelper;
	public static KqDatabaseAdapter sInstance;

	private KqDatabaseAdapter(Context sContext) {
		KqDatabaseAdapter.sContext = sContext;
	}

	public static KqDatabaseAdapter createInstance(Context sContext) {
		if (sInstance == null) {
			sInstance = new KqDatabaseAdapter(sContext);
			open();
		}

		return sInstance;
	}

	private static void open() {
		mHelper = new KQDatabaseHelper(sContext);
		sDb = mHelper.getWritableDatabase();

	}

	private void close() {
		mHelper.close();
	}

	public long inserValue(String post_id) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConstant.POST_ID, post_id);

		try {
			sDb.beginTransaction();
			final long state = sDb
					.insert(DatabaseConstant.TABLE_NAME, null, values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}

	}
	
	public ArrayList<String> fetChValue() {	
		
		ArrayList<String> piArr  = new ArrayList<String>();
		Cursor cursor = sDb.rawQuery("SELECT *  FROM kq",null);		
		if(cursor.getCount()>0){
			piArr.clear();
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				piArr.add(cursor.getString(1));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return piArr;
}

}
