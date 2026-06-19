package com.example.moodee;

import android.content.Context;
import android.content.SharedPreferences;
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
                checkLoginStatus();
            }
        }, 2000);
    }

    private void checkLoginStatus() {
        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);
        String savedPin = pref.getString("app_pin", "");

        if (userId != -1) {
            // User sudah login
            if (!savedPin.isEmpty()) {
                // Ada PIN, minta verifikasi dulu
                Bundle args = new Bundle();
                args.putString("mode", "verify");
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_LandingFragment_to_PinFragment, args);
            } else {
                // Tidak ada PIN, langsung ke Home
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_LandingFragment_to_navigation_home);
            }
        } else {
            // User belum login, ke Onboarding
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_LandingFragment_to_FirstFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
