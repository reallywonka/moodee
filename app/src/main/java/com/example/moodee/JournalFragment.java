package com.example.moodee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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

        // Arahkan ke MoodSelectionFragment dulu sebelum menulis
        binding.fabAddJournal.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_navigation_journal_to_MoodSelectionFragment);
        });
        
        // Setup RecyclerView akan dilakukan di langkah selanjutnya
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
