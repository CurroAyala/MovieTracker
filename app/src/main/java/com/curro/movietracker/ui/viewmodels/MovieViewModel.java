package com.curro.movietracker.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.curro.movietracker.data.repositories.MovieRepository;
import com.curro.movietracker.domain.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final MovieRepository repository;
    private final LiveData<List<Movie>> allMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        allMovies = repository.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public void insert(Movie movie) {
        repository.insert(movie);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }

    public void update(Movie movie) {
        repository.update(movie);
    }

}
