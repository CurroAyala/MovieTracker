package com.curro.movietracker.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.curro.movietracker.data.entities.MovieEntity;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY director ASC")
    LiveData<List<MovieEntity>> getAllMovies();

    @Query("SELECT * FROM movies ORDER BY id ASC")
    List<MovieEntity> getAllMoviesSync();

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<MovieEntity> getMovieById(int id);

    @Query("SELECT COUNT(*) FROM movies WHERE original_title = :originalTitle")
    int countMoviesByOriginalTitle(String originalTitle);

    @Insert
    void createMovie(MovieEntity movie);

    @Update
    void updateMovie(MovieEntity movie);

    @Delete
    void deleteMovie(MovieEntity movie);

}
