package com.example.moodee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moodee.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(ForgotPasswordFragment.this).navigateUp();
        });

        binding.btnReset.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(getContext(), "Please enter your username", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Reset link sent to " + username, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ForgotPasswordFragment.this).navigateUp();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
