package com.frost.dbrom.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 08-08-2019.
 * <p>
 * Frost
 */
@Database(version = 1,entities = {Note.class,User.class})
public abstract class DBRoomProvider extends RoomDatabase
{
    private static DBRoomProvider provider = null;

    public static DBRoomProvider getInstance(Context context)
    {
        if(provider==null)
        {
            synchronized (DBRoomProvider.class){
                provider = Room.databaseBuilder(context,DBRoomProvider.class,"db_notes")
                            .build();
            }
        }

        return provider;
    }

    public abstract NoteDao getNoteDao();

}
