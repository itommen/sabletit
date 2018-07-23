package com.example.ndt.sabletid;

import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private static FirebaseDatabase database;

    public static FirebaseDatabase getInstance() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}
