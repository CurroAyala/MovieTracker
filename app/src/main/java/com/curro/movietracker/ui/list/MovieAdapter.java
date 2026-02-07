package com.curro.movietracker.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.curro.movietracker.R;
import com.curro.movietracker.databinding.MovieItemBinding;
import com.curro.movietracker.domain.model.Movie;

import java.util.Objects;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private final OnItemClickListener listener;

    public MovieAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return Objects.equals(oldItem.getOriginal_title(), newItem.getOriginal_title()) &&
                    Objects.equals(oldItem.getSpanish_title(), newItem.getSpanish_title()) &&
                    Objects.equals(oldItem.getDirector(), newItem.getDirector()) &&
                    Objects.equals(oldItem.getYear(), newItem.getYear());
        }
    };

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemBinding binding = MovieItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = getItem(position);
        holder.bind(currentMovie, listener);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final MovieItemBinding binding;

        public MovieViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie, OnItemClickListener listener) {
            binding.tvOriginalTitle.setText(movie.getOriginal_title());

            if (movie.getYear() != null) {
                binding.tvYear.setText(String.valueOf(movie.getYear()));
            } else {
                binding.tvYear.setText("");
            }

            if (movie.getDirector() != null && !movie.getDirector().isEmpty()) {
                Context context = binding.getRoot().getContext();
                String directorPrefix = context.getString(R.string.prefix_director);
                String text = directorPrefix + " " + movie.getDirector();
                binding.tvDirector.setText(text);
            } else {
                binding.tvDirector.setText("");
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(movie);
                }
            });
        }
    }

}
