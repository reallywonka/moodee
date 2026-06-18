package com.example.moodee;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moodee.databinding.FragmentLandingBinding;

public class LandingFragment extends Fragment {

    private FragmentLandingBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentLandingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Splash screen delay: 2 seconds before moving to the next screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                NavHostFragment.findNavController(LandingFragment.this)
                        .navigate(R.id.action_LandingFragment_to_FirstFragment);
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
