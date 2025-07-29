package com.example.gymlog.viewholders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gymlog.Database.GymLogRepository;
import com.example.gymlog.Database.entities.GymLog;

import java.util.List;

public class GymLogViewModel extends AndroidViewModel {
    private final GymLogRepository repository;

    //private final LiveData<List<GymLog>> allLogsById;

    public GymLogViewModel(Application application) {
        super(application);
        repository = GymLogRepository.getRepository(application);
        //allLogsById = repository.getAllLogsByUserIdLiveData(userId);
    }

    public LiveData<List<GymLog>> getAllLogsById(int userId) {
        return repository.getAllLogsByUserIdLiveData(userId);
    }

    public void insert(GymLog log){
        repository.insertGymLog(log);
    }

}
