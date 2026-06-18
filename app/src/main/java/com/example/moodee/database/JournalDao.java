package com.example.moodee.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JournalDao {
    @Insert
    void insertJournal(Journal journal);

    @Update
    void updateJournal(Journal journal);

    @Delete
    void deleteJournal(Journal journal);

    @Query("SELECT * FROM journals WHERE userId = :userId ORDER BY date DESC")
    List<Journal> getAllJournals(int userId);
}
