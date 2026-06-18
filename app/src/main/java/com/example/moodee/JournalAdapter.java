package com.example.moodee;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodee.database.Journal;
import com.example.moodee.databinding.ItemJournalBinding;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private final List<Journal> journalList;

    public JournalAdapter(List<Journal> journalList) {
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemJournalBinding binding = ItemJournalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new JournalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int viewType) {
        Journal journal = journalList.get(viewType);
        holder.binding.txtItemDate.setText(journal.date);
        holder.binding.txtItemTitle.setText(journal.title);
        holder.binding.txtItemContent.setText(journal.content);
        holder.binding.txtItemMood.setText(journal.mood);
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    static class JournalViewHolder extends RecyclerView.ViewHolder {
        final ItemJournalBinding binding;

        public JournalViewHolder(@NonNull ItemJournalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
