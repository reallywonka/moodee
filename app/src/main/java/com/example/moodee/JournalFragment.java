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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moodee.database.AppDatabase;
import com.example.moodee.database.Journal;
import com.example.moodee.databinding.FragmentJournalBinding;

import java.util.List;

public class JournalFragment extends Fragment implements JournalAdapter.OnJournalClickListener {

    private FragmentJournalBinding binding;
    private JournalAdapter adapter;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);
        db = AppDatabase.getDatabase(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup RecyclerView
        adapter = new JournalAdapter();
        adapter.setOnJournalClickListener(this);
        binding.rvJournals.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvJournals.setAdapter(adapter);

        // 2. Load Data
        loadJournals();

        // 3. Fab Add Journal
        binding.fabAddJournal.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_navigation_journal_to_MoodSelectionFragment);
        });
    }

    private void loadJournals() {
        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            List<Journal> journals = db.journalDao().getAllJournals(userId);
            adapter.setJournals(journals);
        }
    }

    @Override
    public void onJournalClick(Journal journal) {
        // Mode Edit: Kirim ID Jurnal ke WritingFragment
        Bundle args = new Bundle();
        args.putInt("journal_id", journal.id);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_journal_to_WritingFragment, args);
    }

    @Override
    public void onDeleteClick(Journal journal) {
        // Konfirmasi Hapus
        new AlertDialog.Builder(requireContext())
                .setTitle("Hapus Jurnal")
                .setMessage("Apakah kamu yakin ingin menghapus catatan ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    db.journalDao().deleteJournal(journal);
                    loadJournals(); // Refresh list
                    Toast.makeText(getContext(), "Jurnal dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
