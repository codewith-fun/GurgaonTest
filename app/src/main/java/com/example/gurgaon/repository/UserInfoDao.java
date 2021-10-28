package com.example.gurgaon.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gurgaon.model.UserModel;

import java.util.List;

@Dao
public interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserModel userModel);

    @Query("SELECT * from user_info ORDER BY name ASC")
    LiveData<List<UserModel>> getAllUser();

    @Query("select * from user_info WHERE  mob")
    LiveData<List<UserModel>> getMobile();

    @Query("DELETE from user_info")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<UserModel> userModels);
}
