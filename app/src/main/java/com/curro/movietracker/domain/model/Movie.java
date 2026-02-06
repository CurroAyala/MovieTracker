package com.curro.movietracker.domain.model;

import java.io.Serializable;

public class Movie implements Serializable {

    private int id;
    private String original_title;
    private String spanish_title;
    private String director;
    private Integer year;

    public Movie() {

    }

    public Movie(String original_title, String spanish_title, String director, Integer year) {
        this.original_title = original_title;
        this.spanish_title = spanish_title;
        this.director = director;
        this.year = year;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSpanish_title() {
        return spanish_title;
    }

    public void setSpanish_title(String spanish_title) {
        this.spanish_title = spanish_title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
