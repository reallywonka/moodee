package com.example.moodee;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.moodee.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inisialisasi NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        
        // Hubungkan BottomNavigationView dengan NavController
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        // Logika Show/Hide Bottom Navigation
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.HomeFragment) {
                // Tampilkan hanya di Dashboard/Home
                binding.bottomNav.setVisibility(View.VISIBLE);
            } else {
                // Sembunyikan di Landing, Login, Register, dll.
                binding.bottomNav.setVisibility(View.GONE);
            }
        });
    }
}
