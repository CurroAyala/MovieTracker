package com.curro.movietracker.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.curro.movietracker.data.DataMapper;
import com.curro.movietracker.data.daos.MovieDao;
import com.curro.movietracker.data.local.AppDatabase;
import com.curro.movietracker.domain.model.Movie;

import java.util.List;

public class MovieRepository {

    private final MovieDao movieDao;
    private final LiveData<List<Movie>> allMovies;

    public MovieRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        movieDao = db.movieDao();
        allMovies = Transformations.map(movieDao.getAllMovies(), DataMapper::toMovieList);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public void insert(Movie movie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            movieDao.createMovie(DataMapper.movieToMovieEntity(movie));
        });
    }

    public void delete(Movie movie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            movieDao.deleteMovie(DataMapper.movieToMovieEntity(movie));
        });
    }

    public void update(Movie movie) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            movieDao.updateMovie(DataMapper.movieToMovieEntity(movie));
        });
    }

}
