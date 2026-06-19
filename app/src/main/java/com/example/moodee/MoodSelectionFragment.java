package com.example.moodee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moodee.databinding.FragmentMoodSelectionBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoodSelectionFragment extends Fragment {

    private FragmentMoodSelectionBinding binding;
    private String selectedMood = "biasa"; // Default mood

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoodSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Tampilkan Tanggal & Waktu Otomatis (sesuai mockup)
        String currentDate = new SimpleDateFormat("EEEE, dd MMMM", new Locale("id", "ID")).format(new Date());
        String currentTime = new SimpleDateFormat("HH.mm", Locale.getDefault()).format(new Date());
        
        binding.txtMoodDate.setText("Hari Ini, " + currentDate);
        binding.txtMoodTime.setText(currentTime);

        // 2. Logika Klik Mood
        setupMoodClickListeners();

        // 3. Tombol Lanjutkan
        binding.fabContinue.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("selected_mood", selectedMood);
            
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_MoodSelectionFragment_to_WritingFragment, args);
        });

        // 4. Tombol Close
        binding.btnCloseMood.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
    }

    private void setupMoodClickListeners() {
        binding.moodKeren.setOnClickListener(v -> selectMood("keren", binding.imgKeren));
        binding.moodBaik.setOnClickListener(v -> selectMood("baik", binding.imgBaik));
        binding.moodBiasa.setOnClickListener(v -> selectMood("biasa", binding.imgBiasa));
        binding.moodBuruk.setOnClickListener(v -> selectMood("buruk", binding.imgBuruk));
        binding.moodSangatBuruk.setOnClickListener(v -> selectMood("sangat buruk", binding.imgSangatBuruk));
        
        // Pilih mood 'biasa' sebagai default visual
        selectMood("biasa", binding.imgBiasa);
    }

    private void selectMood(String mood, ImageView selectedImage) {
        selectedMood = mood;

        // Reset semua ke alpha 0.5 (transparan)
        binding.imgKeren.setAlpha(0.5f);
        binding.imgBaik.setAlpha(0.5f);
        binding.imgBiasa.setAlpha(0.5f);
        binding.imgBuruk.setAlpha(0.5f);
        binding.imgSangatBuruk.setAlpha(0.5f);

        // Set yang dipilih ke alpha 1.0 (terang)
        selectedImage.setAlpha(1.0f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
