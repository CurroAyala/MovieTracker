package com.curro.movietracker.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.curro.movietracker.databinding.FragmentSettingsBinding;
import com.curro.movietracker.ui.viewmodels.MovieViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private MovieViewModel movieViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        binding.btnSpanish.setOnClickListener(v -> changeLanguage("es"));

        binding.btnEnglish.setOnClickListener(v -> changeLanguage("en"));

        binding.btnExportCsv.setOnClickListener(v -> {
            createCsvLauncher.launch("movie-data.csv");
        });

        binding.btnImportCsv.setOnClickListener(v -> {
            openCsvLauncher.launch(new String[]{"text/csv", "text/comma-separated-values"});
        });
    }

    private final ActivityResultLauncher<String> createCsvLauncher = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("text/csv"),
            uri -> {
                if (uri != null) {
                    movieViewModel.exportCsv(uri, requireContext().getContentResolver());
                }
            }
    );

    private final ActivityResultLauncher<String[]> openCsvLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) {
                    movieViewModel.importCsv(uri, requireContext().getContentResolver());
                }
            }
    );

    private void changeLanguage(String languageCode) {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(languageCode);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

}
