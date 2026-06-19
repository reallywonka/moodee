package com.example.moodee;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moodee.database.AppDatabase;
import com.example.moodee.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ambil data nama dari SharedPreferences (Sesi Login)
        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        String name = pref.getString("name", "User");

        // 2. Tampilkan sapaan dinamis
        binding.txtGreeting.setText("Good Morning, " + name + "!");

        binding.btnStartWriting.setOnClickListener(v -> {
            // Arahkan ke MoodSelectionFragment
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_home_to_MoodSelectionFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
