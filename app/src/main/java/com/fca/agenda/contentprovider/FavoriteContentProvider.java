package com.fca.agenda.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fca.agenda.dao.AgendaContract;
import com.fca.agenda.dao.CommunicationDBHelper;

/**
 * Created by fabiano.alvarenga on 21/04/18.
 */

public class FavoriteContentProvider extends ContentProvider {

    private CommunicationDBHelper dbHelper;
    private SQLiteDatabase database;

    static final String AUTHORITY = "com.fca.agenda";

    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+ AgendaContract.CommunicationEntry.TABLE_NAME+"/#");

    @Override
    public boolean onCreate() {
        this.dbHelper = new CommunicationDBHelper(getContext());
        this.database = dbHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(AgendaContract.CommunicationEntry.TABLE_NAME);

        Cursor cursor =  queryBuilder.query(
                database,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
