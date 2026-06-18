package com.example.moodee.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journals")
public class Journal {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int userId;
    public String date;
    public String title;
    public String content;
    public String mood;
    public String tags;

    public Journal(int userId, String date, String title, String content, String mood, String tags) {
        this.userId = userId;
        this.date = date;
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.tags = tags;
    }
}
