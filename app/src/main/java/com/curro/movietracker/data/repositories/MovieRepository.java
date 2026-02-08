package com.curro.movietracker.data.repositories;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.curro.movietracker.R;
import com.curro.movietracker.data.DataMapper;
import com.curro.movietracker.data.daos.MovieDao;
import com.curro.movietracker.data.entities.MovieEntity;
import com.curro.movietracker.data.local.AppDatabase;
import com.curro.movietracker.domain.model.Movie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class MovieRepository {

    private final MovieDao movieDao;
    private final LiveData<List<Movie>> allMovies;
    private final Application application;

    public MovieRepository(Application application) {
        this.application = application;
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

    // IMPORTACIÓN DESDE CSV
    public void importMoviesFromCsv(Uri uri, ContentResolver contentResolver, OnCsvOperationListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int successCount = 0;
            int errorCount = 0;
            try (InputStream inputStream = contentResolver.openInputStream(uri);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        if (line.toLowerCase().startsWith("original_title")) continue;
                    }

                    String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    if (tokens.length >= 1) {
                        String originalTitle = unescapeCsv(tokens[0].trim());
                        String spanishTitle = tokens.length > 1 ? unescapeCsv(tokens[1].trim()) : "";
                        String director = tokens.length > 2 ? unescapeCsv(tokens[2].trim()) : "";
                        String yearStr = tokens.length > 3 ? tokens[3].trim() : "";

                        if (!originalTitle.isEmpty()) {
                            int existing = movieDao.countMoviesByOriginalTitle(originalTitle);
                            if (existing == 0) {
                                Integer year = null;
                                try {
                                    if (!yearStr.isEmpty()) year = Integer.parseInt(yearStr);
                                } catch (NumberFormatException e) {
                                }

                                MovieEntity entity = new MovieEntity(originalTitle, spanishTitle, director, year);
                                movieDao.createMovie(entity);
                                successCount++;
                            }
                        }
                    } else {
                        errorCount++;
                    }
                }
                String msg = application.getString(R.string.import_success, successCount);
                listener.onSuccess(msg);

            } catch (Exception e) {
                Log.e("CSV_IMPORT", "Error importing CSV", e);
                String errorMsg = application.getString(R.string.import_error, e.getMessage());
                listener.onError(errorMsg);
            }
        });
    }

    // EXPORTACIÓN A CSV
    public void exportMoviesToCsv(Uri uri, ContentResolver contentResolver, OnCsvOperationListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try (OutputStream outputStream = contentResolver.openOutputStream(uri);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

                writer.write("original_title,spanish_title,director,year");
                writer.newLine();

                List<MovieEntity> movies = movieDao.getAllMoviesSync();
                if (movies != null) {
                    for (MovieEntity movie : movies) {
                        String line = String.format("%s,%s,%s,%s",
                                escapeCsv(movie.original_title),
                                escapeCsv(movie.spanish_title),
                                escapeCsv(movie.director),
                                movie.year != null ? movie.year : "");
                        writer.write(line);
                        writer.newLine();
                    }
                }
                String msg = application.getString(R.string.export_success);
                listener.onSuccess(msg);

            } catch (Exception e) {
                Log.e("CSV_EXPORT", "Error exporting CSV", e);
                String errorMsg = application.getString(R.string.export_error, e.getMessage());
                listener.onError(errorMsg);
            }
        });
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unescapeCsv(String value) {
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("\"\"", "\"");
        }
        return value;
    }

    public interface OnCsvOperationListener {
        void onSuccess(String message);
        void onError(String error);
    }

}
