package com.thuan.testniw.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.thuan.testniw.dao.ExampleDao;
import com.thuan.testniw.model.Example;


@Database(entities = Example.class, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ExampleDatabase extends RoomDatabase {
    private static ExampleDatabase exampleDatabase;

    public static synchronized ExampleDatabase getExampleDatabase(Context context) {
        if(exampleDatabase == null) {
            exampleDatabase = Room.databaseBuilder(context, ExampleDatabase.class, "example_db").build();
        }
        return exampleDatabase;
    }

    public abstract ExampleDao exampleDao();
}