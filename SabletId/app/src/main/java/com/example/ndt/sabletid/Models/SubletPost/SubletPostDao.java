package com.example.ndt.sabletid.Models.SubletPost;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SubletPostDao {
    @Query("SELECT * FROM subletPost")
    List<SubletPost> getAll();

    @Query("SELECT * FROM SubletPost where userId LIKE :userId")
    List<SubletPost> getSubletPostsByUserId(String userId);

    @Query("SELECT * FROM SubletPost where id LIKE :id")
    SubletPost getSubletPostById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SubletPost... subletPosts);

    @Update
    void update(SubletPost... subletPosts);

    @Delete
    void delete(SubletPost subletPost);
}
