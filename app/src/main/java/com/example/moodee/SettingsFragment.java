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

        // 1. Ambil data dari Sesi (SharedPreferences)
        android.content.SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", android.content.Context.MODE_PRIVATE);
        int userId = pref.getInt("user_id", -1);
        String name = pref.getString("name", "User");
        String username = pref.getString("username", "username");

        // 2. Tampilkan Nama dan Username ke Layar
        binding.txtProfileName.setText(name);
        binding.txtJoinedDate.setText("@" + username);

        // 3. Ambil data dari Database
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        List<Journal> journals = db.journalDao().getAllJournals(userId);
        
        // Tampilkan jumlah entri
        binding.txtEntriesCount.setText(String.valueOf(journals.size()));
        
        // Hitung dan tampilkan Streak
        int streak = calculateStreak(journals);
        binding.txtStreakCount.setText(String.valueOf(streak));

        // 4. Logika Tombol Setup PIN
        binding.btnSetupPin.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("mode", "create");
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_navigation_settings_to_PinFragment, args);
        });

        // 5. Logika Tombol Logout
        binding.btnLogout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            pref.edit().clear().apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_global_LoginFragment);
        });
    }

    private int calculateStreak(List<Journal> journals) {
        if (journals == null || journals.isEmpty()) return 0;

        // Gunakan HashSet untuk menyimpan tanggal unik (format: dd-MM-yyyy)
        // Kita pakai Locale ID karena format tanggal yang disimpan dalam bahasa Indonesia
        SimpleDateFormat storedFormat = new SimpleDateFormat("EEEE, dd MMMM", new Locale("id", "ID"));
        SimpleDateFormat normalizeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        HashSet<String> uniqueDates = new HashSet<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (Journal j : journals) {
            try {
                Date d = storedFormat.parse(j.date);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.set(Calendar.YEAR, currentYear); // Asumsikan tahun sekarang karena data tidak simpan tahun
                uniqueDates.add(normalizeFormat.format(c.getTime()));
            } catch (Exception e) {
                // Jika gagal parse, lewati
            }
        }

        int streak = 0;
        Calendar checkCal = Calendar.getInstance();
        
        // 1. Cek apakah ada jurnal hari ini
        String today = normalizeFormat.format(checkCal.getTime());
        
        if (!uniqueDates.contains(today)) {
            // Jika hari ini belum nulis, cek kemarin
            checkCal.add(Calendar.DATE, -1);
            String yesterday = normalizeFormat.format(checkCal.getTime());
            if (!uniqueDates.contains(yesterday)) {
                return 0; // Kemarin juga nggak nulis, streak putus
            }
        }

        // 2. Hitung mundur ke belakang
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
