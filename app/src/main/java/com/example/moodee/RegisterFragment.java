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

import com.example.moodee.database.AppDatabase;
import com.example.moodee.database.User;
import com.example.moodee.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        db = AppDatabase.getDatabase(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSignUp.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Check if user already exists
                User existingUser = db.userDao().getUserByUsername(username);
                if (existingUser != null) {
                    Toast.makeText(getContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Save to SQLite
                    db.userDao().registerUser(new User(name, username, password));
                    Toast.makeText(getContext(), "Registration successful for " + name, Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(RegisterFragment.this).navigateUp();
                }
            }
        });

        binding.txtBackToLogin.setOnClickListener(v -> {
            NavHostFragment.findNavController(RegisterFragment.this).navigateUp();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
