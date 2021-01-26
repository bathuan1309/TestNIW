package com.thuan.testniw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.thuan.testniw.model.Example;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface ExampleDao {
    @Query("SELECT * FROM example")
    Flowable<List<Example>> getExampleRoom();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToRoom(List<Example> exampleList);

}
