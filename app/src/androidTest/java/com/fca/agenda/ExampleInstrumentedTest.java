package com.fca.agenda;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.fca.agenda.contentprovider.FavoriteContentProvider;
import com.fca.agenda.dao.AgendaContract;
import com.fca.agenda.dto.CommunicationDTO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void contentProviderTest() throws Exception {

        Context appContext = InstrumentationRegistry.getTargetContext();

        String[] projection = new String[]{
                AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID
        };

        Cursor cursor = appContext.getContentResolver().query(
                FavoriteContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID);

        List<CommunicationDTO> communicationDTOS = null;

        if (null != cursor && cursor.moveToFirst()) {
            communicationDTOS = new ArrayList<>();
            CommunicationDTO dto;
            do {
                dto = new CommunicationDTO();
                dto.setId(cursor.getLong(cursor.getColumnIndexOrThrow(AgendaContract.CommunicationEntry.COLUMN_COMMUNICATION_ID)));
                communicationDTOS.add(dto);
            }
            while (cursor.moveToNext());
        }

        Assert.assertTrue(communicationDTOS.size() > 0);

    }
}
