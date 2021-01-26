package com.thuan.testniw.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.thuan.testniw.database.ExampleDatabase;
import com.thuan.testniw.model.Example;
import com.thuan.testniw.repository.ExampleRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class ExampleViewModel extends AndroidViewModel {
    private ExampleRepository exampleRepository;
    private ExampleDatabase exampleDatabase;

    public ExampleViewModel(@NonNull Application application) {
        super(application);
        exampleRepository = new ExampleRepository();
        exampleDatabase = ExampleDatabase.getExampleDatabase(application);
    }

    public LiveData<List<Example>> getExamples(int page) {
        return exampleRepository.getExamples(page);
    }

    public Completable addToRoom(List<Example> example) {
        return exampleDatabase.exampleDao().addToRoom(example);
    }

    public Flowable<List<Example>> loadExamples() {
        return exampleDatabase.exampleDao().getExampleRoom();
    }
}
