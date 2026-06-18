package com.example.moodee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moodee.database.AppDatabase;
import com.example.moodee.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. Inisialisasi binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ambil data database
        AppDatabase db = AppDatabase.getDatabase(requireContext());

        String username = "Fajar Maulana";

        // Cari TextView sapaan di layoutmu (pastikan ID-nya benar di XML)
        // binding.txtGreeting.setText("Good Morning, " + username);
        binding.btnStartWriting.setOnClickListener(v -> {
            // NavHostFragment.findNavController(this)
            //      .navigate(R.id.action_HomeFragment_to_WritingFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 2. Bersihkan binding agar tidak memory leak
        binding = null;
    }

}
