package com.frost.dbrom.database;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 08-08-2019.
 * <p>
 * Frost
 */
public class TimestampConverter
{
    static DateFormat df = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
}
