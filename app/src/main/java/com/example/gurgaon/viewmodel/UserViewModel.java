package com.example.gurgaon.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gurgaon.model.UserModel;
import com.example.gurgaon.repository.USerRoomRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private USerRoomRepository uSerRoomRepository;
    private LiveData<List<UserModel>> mAllInfo;
//    private final LiveData<List<UserModel>> mObserval;

    public UserViewModel(@NonNull Application application) {
        super(application);
        uSerRoomRepository = new USerRoomRepository(application);
        mAllInfo = uSerRoomRepository.getAllUsers();

    }

    public void insert(UserModel  userModel){
        uSerRoomRepository.insertDetails(userModel);
    }

    public LiveData<List<UserModel>> getUAllInfo(){
        return mAllInfo;
    }
}
