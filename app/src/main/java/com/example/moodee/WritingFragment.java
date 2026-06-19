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
    private String selectedMood = "Neutral"; // Default baru
    private int existingJournalId = -1;

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

        if (getArguments() != null) {
            existingJournalId = getArguments().getInt("journal_id", -1);
            
            if (existingJournalId != -1) {
                loadExistingJournal(existingJournalId);
            } else {
                // Gunakan "Neutral" sebagai fallback jika argument kosong
                selectedMood = getArguments().getString("selected_mood", "Neutral");
                binding.txtDisplayMood.setText(translateOldMood(selectedMood));
                
                String currentDate = new SimpleDateFormat("EEEE, dd MMMM", new Locale("id", "ID")).format(new Date());
                binding.txtCurrentDate.setText(currentDate);
            }
        }

        binding.btnClose.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        binding.btnSaveJournal.setOnClickListener(v -> saveJournal());
    }

    private void loadExistingJournal(int id) {
        Journal journal = db.journalDao().getJournalById(id);
        if (journal != null) {
            binding.etJournalTitle.setText(journal.title);
            binding.etJournalContent.setText(journal.content);
            binding.txtCurrentDate.setText(journal.date);
            
            // Terjemahkan mood lama ke baru untuk tampilan
            String moodDisplay = translateOldMood(journal.mood);
            binding.txtDisplayMood.setText(moodDisplay);
            selectedMood = moodDisplay;
        }
    }

    // Fungsi pembantu untuk mengubah "keren" -> "Amazing", dsb.
    private String translateOldMood(String mood) {
        if (mood == null) return "Neutral";
        switch (mood.toLowerCase()) {
            case "keren": return "Amazing";
            case "baik": return "Good";
            case "biasa": return "Neutral";
            case "buruk": return "Bad";
            case "sangat buruk": return "Awful";
            default: return mood; // Jika sudah benar (Amazing/Good dll) biarkan saja
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

        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            if (existingJournalId != -1) {
                Journal updatedJournal = db.journalDao().getJournalById(existingJournalId);
                if (updatedJournal != null) {
                    updatedJournal.title = title;
                    updatedJournal.content = content;
                    updatedJournal.mood = selectedMood;
                    db.journalDao().updateJournal(updatedJournal);
                    Toast.makeText(getContext(), "Jurnal diperbarui!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Journal newJournal = new Journal(userId, date, title, content, selectedMood, "");
                db.journalDao().insertJournal(newJournal);
                Toast.makeText(getContext(), "Jurnal berhasil disimpan!", Toast.LENGTH_SHORT).show();
            }
            NavHostFragment.findNavController(this).navigate(R.id.navigation_journal);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
