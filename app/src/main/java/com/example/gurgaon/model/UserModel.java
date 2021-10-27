package com.example.gurgaon.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_info")
public class UserModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "mob")
    private int mob;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;


    public int getMob() {
        return mob;
    }

    public void setMob(int mob) {
        this.mob = mob;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String  toString(){
        return "ClassPojo[name = "+getName()+",mobile= "+getMob()+"]";
    }
}
