package com.fca.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fca.agenda.R;
import com.fca.agenda.dto.CommunicationDTO;

/**
 * Created by fabiano.alvarenga on 21/04/18.
 */

public class CommunicationDAO {

    public static final String TAG = CommunicationDAO.class.getSimpleName();

    private CommunicationDBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public CommunicationDAO(@NonNull Context ctx) {
        this.context = ctx;
        this.dbHelper = new CommunicationDBHelper(ctx);
    }

    public void insert(CommunicationDTO communicationDTO) {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID, communicationDTO.getId());
        values.put(AgendaContract.CommunicationEntry.COLUMN_CHECKED, communicationDTO.getChecked());
        values.put(AgendaContract.CommunicationEntry.COLUMN_FAVORITE, communicationDTO.getFavorite());

        long newRowId = database.insert(AgendaContract.CommunicationEntry.TABLE_NAME, null, values);
        Log.i(TAG, context.getString(R.string.insert_record_success));

        database.close();
    }

    public void update(CommunicationDTO communicationDTO) {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID, communicationDTO.getId());
        values.put(AgendaContract.CommunicationEntry.COLUMN_CHECKED, communicationDTO.getChecked());
        values.put(AgendaContract.CommunicationEntry.COLUMN_FAVORITE, communicationDTO.getFavorite());

        String selection = AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID + " = ?";
        String[] selectionArgs = { String.valueOf(communicationDTO.getId()) };

        long rows = database.update(AgendaContract.CommunicationEntry.TABLE_NAME, values, selection, selectionArgs);
        Log.i(TAG, rows + context.getString(R.string.update_success));

        database.close();
    }

    public CommunicationDTO findOne(Long id) {
        database = dbHelper.getReadableDatabase();

        String[] projection = {
                AgendaContract.CommunicationEntry._ID,
                AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID,
                AgendaContract.CommunicationEntry.COLUMN_FAVORITE,
                AgendaContract.CommunicationEntry.COLUMN_CHECKED
        };

        String selection = AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String sortOrder =
                AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID + " DESC";

        Cursor cursor = database.query(
                AgendaContract.CommunicationEntry.TABLE_NAME,
                projection, selection, selectionArgs, null, null, sortOrder
        );

        CommunicationDTO communicationDTO = null;

        cursor.moveToFirst();
        if(null != cursor && cursor.getCount() > 0) {
            communicationDTO = new CommunicationDTO();
            communicationDTO.setId(cursor.getLong(
                    cursor.getColumnIndexOrThrow(AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID)
            ));
            communicationDTO.setFavorite(cursor.getInt(
                    cursor.getColumnIndexOrThrow(AgendaContract.CommunicationEntry.COLUMN_FAVORITE)
            ) == 1);
            communicationDTO.setChecked(cursor.getInt(
                    cursor.getColumnIndexOrThrow(AgendaContract.CommunicationEntry.COLUMN_CHECKED)
            ) == 1);
        }
        cursor.close();
        database.close();
        return communicationDTO;
    }

    public void delete(Long id) {
        database = dbHelper.getWritableDatabase();

        String selection = AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        int rows = database.delete(AgendaContract.CommunicationEntry.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, rows + context.getString(R.string.deleted_success));

        database.close();
    }

}
