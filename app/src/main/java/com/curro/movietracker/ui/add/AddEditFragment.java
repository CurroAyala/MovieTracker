package com.curro.movietracker.ui.add;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.curro.movietracker.R;
import com.curro.movietracker.databinding.FragmentAddEditBinding;
import com.curro.movietracker.domain.model.Movie;
import com.curro.movietracker.ui.viewmodels.MovieViewModel;

public class AddEditFragment extends Fragment {

    private FragmentAddEditBinding binding;
    private MovieViewModel movieViewModel;
    private Movie currentMovie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        if (getArguments() != null) {
            currentMovie = (Movie) getArguments().getSerializable("movie");
        }

        if (currentMovie != null) {
            binding.etOriginalTitle.setText(currentMovie.getOriginal_title());
            binding.etSpanishTitle.setText(currentMovie.getSpanish_title());
            binding.etDirector.setText(currentMovie.getDirector());
            if (currentMovie.getYear() != null) {
                binding.etYear.setText(String.valueOf(currentMovie.getYear()));
            }
            binding.btnSave.setText(R.string.btn_save);
        }

        binding.btnSave.setOnClickListener(v -> saveMovie());
    }

    private void saveMovie() {
        String originalTitle = binding.etOriginalTitle.getText().toString().trim();
        String spanishTitle = binding.etSpanishTitle.getText().toString().trim();
        String director = binding.etDirector.getText().toString().trim();
        String yearStr = binding.etYear.getText().toString().trim();

        if (TextUtils.isEmpty(originalTitle)) {
            binding.tilOriginalTitle.setError(getString(R.string.error_required_field));
            return;
        } else {
            binding.tilOriginalTitle.setError(null);
        }

        Integer year = null;
        if (!yearStr.isEmpty()) {
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                binding.tilYear.setError(getString(R.string.invalid_year));
                return;
            }
        }

        if (currentMovie == null) {
            Movie newMovie = new Movie(originalTitle, spanishTitle, director, year);
            movieViewModel.insert(newMovie);
            Toast.makeText(getContext(), getString(R.string.movie_saved), Toast.LENGTH_SHORT).show();
        } else {
            currentMovie.setOriginal_title(originalTitle);
            currentMovie.setSpanish_title(spanishTitle);
            currentMovie.setDirector(director);
            currentMovie.setYear(year);

            movieViewModel.update(currentMovie);
            Toast.makeText(getContext(), getString(R.string.movie_updated), Toast.LENGTH_SHORT).show();
        }

        Navigation.findNavController(getView()).popBackStack();
    }

}
