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
    private int existingJournalId = -1; // -1 means new journal

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

        // 1. Cek apakah ini Mode Edit atau Mode Baru
        if (getArguments() != null) {
            existingJournalId = getArguments().getInt("journal_id", -1);
            
            if (existingJournalId != -1) {
                // MODE EDIT: Ambil data dari DB
                loadExistingJournal(existingJournalId);
            } else {
                // MODE BARU: Ambil mood dari MoodSelectionFragment
                selectedMood = getArguments().getString("selected_mood", "biasa");
                binding.txtDisplayMood.setText(selectedMood);
                
                // Tampilkan Tanggal Otomatis (Hanya untuk jurnal baru)
                String currentDate = new SimpleDateFormat("EEEE, dd MMMM", new Locale("id", "ID")).format(new Date());
                binding.txtCurrentDate.setText(currentDate);
            }
        }

        // 2. Tombol Close (X)
        binding.btnClose.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        // 3. Tombol Save
        binding.btnSaveJournal.setOnClickListener(v -> saveJournal());
    }

    private void loadExistingJournal(int id) {
        Journal journal = db.journalDao().getJournalById(id);
        if (journal != null) {
            binding.etJournalTitle.setText(journal.title);
            binding.etJournalContent.setText(journal.content);
            binding.txtCurrentDate.setText(journal.date);
            binding.txtDisplayMood.setText(journal.mood);
            selectedMood = journal.mood;
        }
    }

    private void saveJournal() {
        String title = binding.etJournalTitle.getText().toString().trim();
        String content = binding.etJournalContent.getText().toString().trim();
        String date = binding.txtCurrentDate.getText().toString();

        if (content.isEmpty()) {
            Toast.makeText(getContext(), "Ceritakan harimu dulu ya!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil userId dari sesi login
        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            if (existingJournalId != -1) {
                // UPDATE: Jurnal yang sudah ada
                Journal updatedJournal = db.journalDao().getJournalById(existingJournalId);
                if (updatedJournal != null) {
                    updatedJournal.title = title;
                    updatedJournal.content = content;
                    updatedJournal.mood = selectedMood;
                    // Kita biarkan tanggal aslinya tidak berubah, atau bisa diupdate jika mau
                    db.journalDao().updateJournal(updatedJournal);
                    Toast.makeText(getContext(), "Jurnal diperbarui!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // INSERT: Jurnal baru
                Journal newJournal = new Journal(userId, date, title, content, selectedMood, "");
                db.journalDao().insertJournal(newJournal);
                Toast.makeText(getContext(), "Jurnal berhasil disimpan!", Toast.LENGTH_SHORT).show();
            }
            
            // Kembali ke halaman daftar jurnal
            NavHostFragment.findNavController(this).navigate(R.id.navigation_journal);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
