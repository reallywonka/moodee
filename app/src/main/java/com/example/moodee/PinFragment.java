package com.example.moodee;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.moodee.databinding.FragmentPinBinding;

import java.util.ArrayList;
import java.util.List;

public class PinFragment extends Fragment {

    private FragmentPinBinding binding;
    private StringBuilder currentPin = new StringBuilder();
    private List<ImageView> dots = new ArrayList<>();
    
    private String mode = "verify"; // "verify" or "create"
    private String firstEntry = ""; // For confirming new PIN

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPinBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dots.add(binding.dot1);
        dots.add(binding.dot2);
        dots.add(binding.dot3);
        dots.add(binding.dot4);

        if (getArguments() != null) {
            mode = getArguments().getString("mode", "verify");
        }

        updateUI();
        setupKeypad();
    }

    private void updateUI() {
        if (mode.equals("create")) {
            binding.txtPinTitle.setText("Buat PIN Baru");
            binding.txtPinHint.setText("Masukkan 4 digit PIN keamanan");
        } else {
            binding.txtPinTitle.setText("Masukkan PIN");
            binding.txtPinHint.setText("Gunakan PIN untuk membuka jurnal");
        }
    }

    private void setupKeypad() {
        View.OnClickListener listener = v -> {
            if (currentPin.length() < 4) {
                currentPin.append(((android.widget.Button) v).getText());
                updateDots();
                if (currentPin.length() == 4) {
                    processPin();
                }
            }
        };

        binding.btn1.setOnClickListener(listener);
        binding.btn2.setOnClickListener(listener);
        binding.btn3.setOnClickListener(listener);
        binding.btn4.setOnClickListener(listener);
        binding.btn5.setOnClickListener(listener);
        binding.btn6.setOnClickListener(listener);
        binding.btn7.setOnClickListener(listener);
        binding.btn8.setOnClickListener(listener);
        binding.btn9.setOnClickListener(listener);
        binding.btn0.setOnClickListener(listener);

        binding.btnBackspace.setOnClickListener(v -> {
            if (currentPin.length() > 0) {
                currentPin.deleteCharAt(currentPin.length() - 1);
                updateDots();
            }
        });
    }

    private void updateDots() {
        for (int i = 0; i < dots.size(); i++) {
            if (i < currentPin.length()) {
                dots.get(i).setAlpha(1.0f);
            } else {
                dots.get(i).setAlpha(0.3f);
            }
        }
    }

    private void processPin() {
        SharedPreferences pref = requireContext().getSharedPreferences("moodee_pref", Context.MODE_PRIVATE);
        String savedPin = pref.getString("app_pin", "");

        if (mode.equals("create")) {
            if (firstEntry.isEmpty()) {
                // First step of creation
                firstEntry = currentPin.toString();
                currentPin.setLength(0);
                updateDots();
                binding.txtPinTitle.setText("Konfirmasi PIN");
                binding.txtPinHint.setText("Masukkan ulang PIN yang sama");
            } else {
                // Confirming step
                if (currentPin.toString().equals(firstEntry)) {
                    pref.edit().putString("app_pin", currentPin.toString()).apply();
                    Toast.makeText(getContext(), "PIN berhasil dibuat!", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigateUp();
                } else {
                    Toast.makeText(getContext(), "PIN tidak cocok, coba lagi", Toast.LENGTH_SHORT).show();
                    currentPin.setLength(0);
                    firstEntry = "";
                    updateUI();
                    updateDots();
                }
            }
        } else {
            // Verification mode
            if (currentPin.toString().equals(savedPin)) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_PinFragment_to_navigation_home);
            } else {
                Toast.makeText(getContext(), "PIN Salah!", Toast.LENGTH_SHORT).show();
                currentPin.setLength(0);
                updateDots();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
