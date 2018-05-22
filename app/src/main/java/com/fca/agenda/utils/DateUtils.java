package com.fca.agenda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fabiano.alvarenga on 10/22/17.
 */

public class DateUtils {

    private static SimpleDateFormat format;

    /**
     * Method responsible for receiving a date and returning
     * a formatted string (dd/MM/yyyy)
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    /**
     * Method responsible for receiving a string in date format (dd/MM/yyyy)
     * and returning an instance of Date
     *
     * @param date
     * @return
     */
    public static Date stringToDate(String date) {
        format = new SimpleDateFormat("dd/MM/yyyy");
        Date result = null;
        try {
            result = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Method responsible for receiving an instance of Date and
     * returning its corresponding year
     *
     * @param date
     * @return
     */
    public static String getYearDate(Date date) {
        String dateString = dateToString(date);
        return dateString.substring(6,10);
    }

}
