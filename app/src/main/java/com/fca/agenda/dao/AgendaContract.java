package com.fca.agenda.dao;

import android.provider.BaseColumns;

/**
 * Created by fabiano.alvarenga on 21/04/18.
 */

public class AgendaContract {

    public static class CommunicationEntry implements BaseColumns {
        public static String TABLE_NAME = "communication";
        public static String COLUMN_COMMUNICATION_ID = "communicationId";
        public static String COLUMN_FAVORITE = "favorite";
        public static String COLUMN_CHECKED = "checked";
    }
}
