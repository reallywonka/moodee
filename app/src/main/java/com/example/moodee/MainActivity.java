package com.example.moodee;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moodee.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar dan FAB dihapus untuk tampilan yang lebih bersih (Zen) sesuai mockup
    }
}
