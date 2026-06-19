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
import com.example.moodee.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        db = AppDatabase.getDatabase(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSignIn.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = db.userDao().login(username, password);
            if (user != null) {
                // Simpan sesi user (Termasuk Nama)
                android.content.SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", android.content.Context.MODE_PRIVATE);
                pref.edit()
                        .putInt("user_id", user.id)
                        .putString("username", user.username)
                        .putString("name", user.name)
                        .apply();

                Toast.makeText(getContext(), "Login Successful! Welcome " + user.name, Toast.LENGTH_SHORT).show();
                // Pindah ke Home menggunakan ID baru yang sesuai dengan Bottom Nav
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_navigation_home);
            } else {
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtSignUp.setOnClickListener(v -> {
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_LoginFragment_to_RegisterFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
