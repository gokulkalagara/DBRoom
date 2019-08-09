package com.frost.dbrom.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 08-08-2019.
 * <p>
 * Frost
 */

@Entity(tableName = "note",indices = {@Index(value = "title",unique = true)})
public class Note implements Parcelable
{
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;


    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;


    private String time;

//    @ColumnInfo(name = "time_created")
//    @TypeConverters({TimestampConverter.class})
//    private Date timeCreated;
//
//    @ColumnInfo(name = "time_notify")
//    @TypeConverters({TimestampConverter.class})
//    private Date timeNotify;

    public Note()
    {

    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public Date getTimeCreated() {
//        return timeCreated;
//    }
//
//    public void setTimeCreated(Date timeCreated) {
//        this.timeCreated = timeCreated;
//    }
//
//    public Date getTimeNotify() {
//        return timeNotify;
//    }
//
//    public void setTimeNotify(Date timeNotify) {
//        this.timeNotify = timeNotify;
//    }





}
