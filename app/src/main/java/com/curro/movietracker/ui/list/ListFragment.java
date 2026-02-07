package com.curro.movietracker.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.curro.movietracker.R;
import com.curro.movietracker.databinding.FragmentListBinding;
import com.curro.movietracker.ui.viewmodels.MovieViewModel;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private MovieViewModel movieViewModel;
    private MovieAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        movieViewModel.getAllMovies().observe(getViewLifecycleOwner(), movies -> {
            adapter.submitList(movies);
        });

        binding.fabAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_list_to_add_edit);
        });
    }

    private void setupRecyclerView() {

        adapter = new MovieAdapter(movie -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("movie", movie);
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_list_to_add_edit,
                    bundle
            );
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
