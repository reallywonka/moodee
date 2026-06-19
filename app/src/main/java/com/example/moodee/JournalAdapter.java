package com.example.moodee;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodee.database.Journal;
import com.example.moodee.databinding.ItemJournalBinding;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<Journal> journalList = new ArrayList<>();
    private OnJournalClickListener listener;

    public interface OnJournalClickListener {
        void onJournalClick(Journal journal);
        void onDeleteClick(Journal journal);
    }

    public void setOnJournalClickListener(OnJournalClickListener listener) {
        this.listener = listener;
    }

    public void setJournals(List<Journal> journals) {
        this.journalList = journals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemJournalBinding binding = ItemJournalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new JournalViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        holder.bind(journal, listener);
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    static class JournalViewHolder extends RecyclerView.ViewHolder {
        private final ItemJournalBinding binding;

        public JournalViewHolder(ItemJournalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Journal journal, OnJournalClickListener listener) {
            binding.txtItemTitle.setText(journal.title);
            binding.txtItemDate.setText(journal.date);
            binding.txtItemContent.setText(journal.content);

            // Set mood icon (Gunakan ignore case agar lebih aman)
            int moodResId;
            String mood = journal.mood != null ? journal.mood : "Neutral";

            switch (mood) {
                case "Amazing":
                    moodResId = R.drawable.cool_emoji;
                    break;
                case "Good":
                    moodResId = R.drawable.happy_emoji;
                    break;
                case "Bad":
                    moodResId = R.drawable.bad_emoji;
                    break;
                case "Awful":
                    moodResId = R.drawable.reallybad_emoji;
                    break;
                case "Neutral":
                default:
                    moodResId = R.drawable.neutral_emoji;
                    break;
            }
            binding.imgItemMood.setImageResource(moodResId);

            // Click listener for the whole item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onJournalClick(journal);
                }
            });

            // Click listener for delete button
            binding.btnDeleteJournal.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(journal);
                }
            });
        }
    }
}
