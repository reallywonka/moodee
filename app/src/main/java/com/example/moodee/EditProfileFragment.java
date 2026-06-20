package com.example.moodee;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.moodee.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private AppDatabase db;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        db = AppDatabase.getDatabase(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ambil userId dari sesi
        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            currentUser = db.userDao().getUserById(userId);
            if (currentUser != null) {
                binding.etEditName.setText(currentUser.name);
                binding.etEditUsername.setText(currentUser.username);
            }
        }

        // 2. Tombol Back
        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        // 3. Tombol Save
        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String newName = binding.etEditName.getText().toString().trim();
        String newUsername = binding.etEditUsername.getText().toString().trim();

        if (newName.isEmpty() || newUsername.isEmpty()) {
            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser != null) {
            currentUser.name = newName;
            currentUser.username = newUsername;

            // Update di Database
            db.userDao().updateUser(currentUser);

            // Update di SharedPreferences (Sesi)
            SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
            pref.edit()
                    .putString("name", newName)
                    .putString("username", newUsername)
                    .apply();

            Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
