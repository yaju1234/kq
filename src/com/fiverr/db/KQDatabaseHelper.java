package com.fiverr.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KQDatabaseHelper extends SQLiteOpenHelper {

	public KQDatabaseHelper(Context context) {
		super(context, DatabaseConstant.DATABASE_NAME, null,
				DatabaseConstant.DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		KqDatabaseCreate.onCreate(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		KqDatabaseCreate.onUpgrade(db, oldVersion, newVersion);

	}

}
