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
import com.example.moodee.database.Journal;
import com.example.moodee.databinding.FragmentWritingBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WritingFragment extends Fragment {

    private FragmentWritingBinding binding;
    private AppDatabase db;
    private String selectedMood = "biasa"; // Default

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWritingBinding.inflate(inflater, container, false);
        db = AppDatabase.getDatabase(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ambil data mood dari arguments (dikirim dari MoodSelectionFragment)
        if (getArguments() != null) {
            selectedMood = getArguments().getString("selected_mood", "biasa");
        }
        binding.txtDisplayMood.setText(selectedMood);

        // 2. Tampilkan Tanggal Otomatis
        String currentDate = new SimpleDateFormat("EEEE, dd MMMM", new Locale("id", "ID")).format(new Date());
        binding.txtCurrentDate.setText(currentDate);

        // 3. Tombol Close (X)
        binding.btnClose.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        // 4. Tombol Save
        binding.btnSaveJournal.setOnClickListener(v -> saveJournal(currentDate));
    }

    private void saveJournal(String date) {
        String title = binding.etJournalTitle.getText().toString().trim();
        String content = binding.etJournalContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(getContext(), "Ceritakan harimu dulu ya!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil userId dari sesi login
        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            // Simpan ke SQLite
            Journal newJournal = new Journal(userId, date, title, content, selectedMood, "");
            db.journalDao().insertJournal(newJournal);
            
            Toast.makeText(getContext(), "Jurnal berhasil disimpan!", Toast.LENGTH_SHORT).show();
            
            // Kembali ke halaman daftar jurnal (Pop up ke Journals)
            NavHostFragment.findNavController(this).navigate(R.id.navigation_journal);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
