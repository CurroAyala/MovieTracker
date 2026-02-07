package com.curro.movietracker.data;

import com.curro.movietracker.data.entities.MovieEntity;
import com.curro.movietracker.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class DataMapper {

    public static Movie movieEntityToMovie(MovieEntity entity) {
        if (entity == null) return null;
        Movie movie = new Movie(
            entity.original_title,
            entity.spanish_title,
            entity.director,
            entity.year
        );
        movie.setId(entity.id);
        return movie;
    }

    public static MovieEntity movieToMovieEntity(Movie movie) {
        if (movie == null) return null;
        MovieEntity entity = new MovieEntity(
            movie.getOriginal_title(),
            movie.getSpanish_title(),
            movie.getDirector(),
            movie.getYear()
        );
        if (movie.getId() != 0) entity.id = movie.getId();
        return entity;
    }

    public static List<Movie> toMovieList(List<MovieEntity> entities) {
        List<Movie> res = new ArrayList<>();
        for (MovieEntity entity : entities) {
            res.add(movieEntityToMovie(entity));
        }
        return res;
    }
}
