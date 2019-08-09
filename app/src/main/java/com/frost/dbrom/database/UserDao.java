package com.frost.dbrom.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 08-08-2019.
 * <p>
 * Frost
 */
@Dao
public interface UserDao
{
    @Insert
    public void insert(User user);

    public List<User> getUsers();
}
