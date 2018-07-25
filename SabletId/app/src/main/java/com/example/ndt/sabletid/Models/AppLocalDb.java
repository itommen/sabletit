package com.example.ndt.sabletid.Models;

import android.arch.persistence.room.Room;

public class AppLocalDb {
    static public AppDatabase db =
            Room.databaseBuilder(SubletItApplication.context, AppDatabase.class,"subletit-database")
                    .fallbackToDestructiveMigration()
                    .build();
}
