package com.example.moodee;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.moodee.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private long backPressedTime;
    private Toast backToast;

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
            int id = destination.getId();
            if (id == R.id.navigation_home || id == R.id.navigation_journal || id == R.id.navigation_settings) {
                // Tampilkan di halaman utama
                binding.bottomNav.setVisibility(View.VISIBLE);
            } else {
                // Sembunyikan di Landing, Login, Register, dll.
                binding.bottomNav.setVisibility(View.GONE);
            }
        });
    }
}
