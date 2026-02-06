package com.curro.movietracker.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "original_title")
    public String original_title;
    @ColumnInfo(name = "spanish_title")
    public String spanish_title;
    @ColumnInfo(name = "director")
    public String director;
    @ColumnInfo(name = "year")
    public Integer year;

    public MovieEntity() { }

    public MovieEntity(String original_title, String spanish_title, String director, Integer year) {
        this.original_title = original_title;
        this.spanish_title = spanish_title;
        this.director = director;
        this.year = year;
    }

}
