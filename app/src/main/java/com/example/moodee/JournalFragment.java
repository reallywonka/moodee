package com.example.moodee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moodee.databinding.FragmentJournalBinding;

public class JournalFragment extends Fragment {

    private FragmentJournalBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJournalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabAddJournal.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Add Journal Clicked", Toast.LENGTH_SHORT).show();
            // Nanti arahkan ke halaman tulis jurnal
        });
        
        // Setup RecyclerView akan dilakukan di langkah selanjutnya
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
