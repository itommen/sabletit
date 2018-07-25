package com.example.ndt.sabletid.Models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

public class AppDatabase {
    @Database(entities = {User.class}, version = 1)
    public abstract class AppDatabase extends RoomDatabase {
        public abstract UserDao userDao();
    }

}
