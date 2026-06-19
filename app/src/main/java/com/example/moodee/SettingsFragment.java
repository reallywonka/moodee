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

import java.util.List;

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

        // 3. Ambil data dari Database untuk menghitung jumlah entri
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        List<Journal> journals = db.journalDao().getAllJournals(userId);
        binding.txtEntriesCount.setText(String.valueOf(journals.size()));

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
            // Hapus sesi saat logout (PIN juga akan terhapus jika clear() semua, 
            // atau biarkan jika ingin PIN tetap ada untuk user tersebut)
            pref.edit().clear().apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_global_LoginFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
