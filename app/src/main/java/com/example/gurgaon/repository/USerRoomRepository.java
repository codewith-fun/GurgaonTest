package com.example.gurgaon.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gurgaon.model.UserModel;

import java.util.List;

public class USerRoomRepository {
    private UserInfoDao userInfoDao;
    private LiveData<List<UserModel>> mAllUsers;

    public USerRoomRepository(Application application) {
        UserrInfoRoomDatabase database =UserrInfoRoomDatabase.getDatabase(application);
        userInfoDao = database.userInfoDao();
        mAllUsers = userInfoDao.getAllUser();
    }

    public LiveData<List<UserModel>> getAllUsers(){
        return mAllUsers;
    }

    public void insert(List<UserModel> userModels){
        new InsertAsyncTask(userInfoDao).execute(userModels);
    }

    public void insertDetails(UserModel data){
        new InsertAsyncTask2(userInfoDao).execute(data);
    }

    private class InsertAsyncTask  extends AsyncTask<List<UserModel>,Void,Void> {
        private UserInfoDao mAsynTask;

        public InsertAsyncTask(UserInfoDao userInfoDao) {
            mAsynTask = userInfoDao;
        }

        @Override
        protected Void doInBackground(final List<UserModel>... lists) {
            mAsynTask.insertUsers(lists[0]);
            return null;
        }
    }

    private class InsertAsyncTask2  extends AsyncTask<UserModel,Void,Void>{
        private UserInfoDao  mAsync;
        public InsertAsyncTask2(UserInfoDao userInfoDao) {
        mAsync = userInfoDao;
        }

        @Override
        protected Void doInBackground(UserModel... userModels) {

            mAsync.insert(userModels[0]);
            return null;
        }
    }
}
