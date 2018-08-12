package com.example.ndt.sabletid.Models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.ndt.sabletid.Models.User.UserDao;
import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.SubletItApplication;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance() {
        synchronized (sLock) {
            if (instance == null) {
                instance = Room.databaseBuilder(SubletItApplication.context,
                        AppDatabase.class, "subletit-database.db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
        }
    }
}
