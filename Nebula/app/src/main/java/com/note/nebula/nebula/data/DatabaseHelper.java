package com.note.nebula.nebula.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.note.nebula.nebula.utilities.Constants;

/**
 * Created by Valentine on 9/28/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "nebula.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_TABLE_NOTE);
                db.execSQL(CREATE_TABLE_USER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + Constants.NOTES_TABLE);
                db.execSQL("DROP TABLE IF EXISTS " + Constants.CREDENTIALS_TABLE);
                onCreate(db);
        }



        private static final String CREATE_TABLE_NOTE = "create table "
            + Constants.NOTES_TABLE
            + "("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_TITLE + " text not null, "
            + Constants.COLUMN_CONTENT + " blob not null, "
            + Constants.COLUMN_MODIFIED_TIME + " integer not null, "
            + Constants.COLUMN_CREATED_TIME + " integer not null " + ")";

        private static final String CREATE_TABLE_USER = "create table "
                + Constants.CREDENTIALS_TABLE
                + "("
                + Constants.COLUMN_PASSWORD + " blob primary key " + ")";
}
