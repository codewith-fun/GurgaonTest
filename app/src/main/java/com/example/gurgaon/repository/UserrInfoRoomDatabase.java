package com.example.gurgaon.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gurgaon.model.UserModel;

@Database( entities = {UserModel.class},exportSchema = false,version = 1)
public abstract class UserrInfoRoomDatabase extends RoomDatabase {

    public abstract UserInfoDao userInfoDao();

    private static UserrInfoRoomDatabase  INSTANCE;


    static UserrInfoRoomDatabase getDatabase(final Context context){

        if (INSTANCE == null){
            synchronized (UserrInfoRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                    ,UserrInfoRoomDatabase.class,"userinfo")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateAsync(INSTANCE).execute();
        }
    };

    private static class PopulateAsync extends AsyncTask<Void,Void,Void> {
        private final UserInfoDao dao;

        public PopulateAsync(UserrInfoRoomDatabase db) {
            dao = db.userInfoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            dao.deleteAll();
            return null;
        }
    }
}
