package com.example.moodee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moodee.databinding.FragmentSettingsBinding;
import com.example.moodee.database.AppDatabase;
import com.example.moodee.database.Journal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserData();

        // 3. Ambil data dari Database
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        
        // Load data on resume/start
        refreshStats();

        // 4. Logika Tombol Edit Profile
        binding.btnEditProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_navigation_settings_to_EditProfileFragment);
        });

        // 5. Logika Tombol Setup PIN
        binding.btnSetupPin.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("mode", "create");
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_navigation_settings_to_PinFragment, args);
        });

        // 6. Logika Tombol Logout
        binding.btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            android.content.SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", android.content.Context.MODE_PRIVATE);
            pref.edit().clear().apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_global_LoginFragment);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
        refreshStats();
    }

    private void loadUserData() {
        android.content.SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", android.content.Context.MODE_PRIVATE);
        String name = pref.getString("name", "User");
        String username = pref.getString("username", "username");

        binding.txtProfileName.setText(name);
        binding.txtJoinedDate.setText("@" + username);
    }

    private void refreshStats() {
        android.content.SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", android.content.Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);

        if (userId != -1) {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            List<Journal> journals = db.journalDao().getAllJournals(userId);
            binding.txtEntriesCount.setText(String.valueOf(journals.size()));
            
            int streak = calculateStreak(journals);
            binding.txtStreakCount.setText(String.valueOf(streak));
        }
    }

    private int calculateStreak(List<Journal> journals) {
        if (journals == null || journals.isEmpty()) return 0;

        SimpleDateFormat storedFormat = new SimpleDateFormat("EEEE, dd MMMM", new Locale("id", "ID"));
        SimpleDateFormat normalizeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        HashSet<String> uniqueDates = new HashSet<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (Journal j : journals) {
            try {
                Date d = storedFormat.parse(j.date);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.set(Calendar.YEAR, currentYear);
                uniqueDates.add(normalizeFormat.format(c.getTime()));
            } catch (Exception e) {}
        }

        int streak = 0;
        Calendar checkCal = Calendar.getInstance();
        String today = normalizeFormat.format(checkCal.getTime());
        
        if (!uniqueDates.contains(today)) {
            checkCal.add(Calendar.DATE, -1);
            String yesterday = normalizeFormat.format(checkCal.getTime());
            if (!uniqueDates.contains(yesterday)) return 0;
        }

        while (uniqueDates.contains(normalizeFormat.format(checkCal.getTime()))) {
            streak++;
            checkCal.add(Calendar.DATE, -1);
        }
        return streak;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
