package com.fca.agenda.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fabiano.alvarenga on 21/04/18.
 */

public class CommunicationDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CommunicationCache.db";

    private static final String NUMERIC_TYPE = " NUMERIC";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AgendaContract.CommunicationEntry.TABLE_NAME + " (" +
                    AgendaContract.CommunicationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID + NUMERIC_TYPE + COMMA_SEP +
                    AgendaContract.CommunicationEntry.COLUMN_FAVORITE + INTEGER_TYPE + COMMA_SEP +
                    AgendaContract.CommunicationEntry.COLUMN_CHECKED + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AgendaContract.CommunicationEntry.TABLE_NAME;

    public CommunicationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
