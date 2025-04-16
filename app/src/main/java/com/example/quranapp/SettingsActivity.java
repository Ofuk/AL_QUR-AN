// File: java/com/example/quranapp/SettingsActivity.java
package com.example.quranapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox cbAutoPlay, cbReminder;
    private SeekBar sbTextSize, sbAudioSpeed;
    private TextView tvTextSizeValue, tvAudioSpeedValue;
    private Button btnClearCache;
    private Switch swDarkMode;
    private Spinner spLanguage;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("QuranAppPrefs", MODE_PRIVATE);

        // Inisialisasi komponen
        cbAutoPlay = findViewById(R.id.cbAutoPlay);
        sbTextSize = findViewById(R.id.sbTextSize);
        tvTextSizeValue = findViewById(R.id.tvTextSizeValue);
        btnClearCache = findViewById(R.id.btnClearCache);
        swDarkMode = findViewById(R.id.swDarkMode);
        spLanguage = findViewById(R.id.spLanguage);
        cbReminder = findViewById(R.id.cbReminder);
        sbAudioSpeed = findViewById(R.id.sbAudioSpeed);
        tvAudioSpeedValue = findViewById(R.id.tvAudioSpeedValue);

        // Setup Spinner Bahasa
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage.setAdapter(adapter);
        String savedLanguage = sharedPreferences.getString("language", "Indonesia");
        spLanguage.setSelection(adapter.getPosition(savedLanguage));
        spLanguage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("language", parent.getItemAtPosition(position).toString());
                editor.apply();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Muat pengaturan yang tersimpan
        cbAutoPlay.setChecked(sharedPreferences.getBoolean("autoPlay", false));
        int savedTextSize = sharedPreferences.getInt("textSize", 16);
        sbTextSize.setProgress(savedTextSize - 12); // Rentang 12sp-24sp
        tvTextSizeValue.setText(savedTextSize + " sp");

        swDarkMode.setChecked(sharedPreferences.getBoolean("darkMode", false));
        cbReminder.setChecked(sharedPreferences.getBoolean("reminder", false));
        float savedAudioSpeed = sharedPreferences.getFloat("audioSpeed", 1.0f);
        sbAudioSpeed.setProgress((int) ((savedAudioSpeed - 0.5f) * 20)); // Rentang 0.5x-2.0x
        tvAudioSpeedValue.setText(String.format("%.1fx", savedAudioSpeed));

        // Listener untuk CheckBox Auto Play
        cbAutoPlay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("autoPlay", isChecked);
            editor.apply();
        });

        // Listener untuk SeekBar Text Size
        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int textSize = progress + 12; // Minimum 12sp, maksimum 24sp
                tvTextSizeValue.setText(textSize + " sp");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("textSize", textSize);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Listener untuk Switch Dark Mode
        swDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("darkMode", isChecked);
            editor.apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Listener untuk CheckBox Reminder
        cbReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("reminder", isChecked);
            editor.apply();
            if (isChecked) {
                scheduleDailyReminder();
            } else {
                WorkManager.getInstance(this).cancelAllWorkByTag("dailyReminder");
            }
        });

        // Listener untuk SeekBar Audio Speed
        sbAudioSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speed = 0.5f + (progress / 20.0f) * 1.5f; // Rentang 0.5x-2.0x
                tvAudioSpeedValue.setText(String.format("%.1fx", speed));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("audioSpeed", speed);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Listener untuk tombol Clear Cache
        btnClearCache.setOnClickListener(v -> {
            WorkManager.getInstance(this).pruneWork();
            android.widget.Toast.makeText(this, "Cache telah dibersihkan", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void scheduleDailyReminder() {
        PeriodicWorkRequest reminderRequest = new PeriodicWorkRequest.Builder(ReminderWorker.class, 1, TimeUnit.DAYS)
                .addTag("dailyReminder")
                .build();
        WorkManager.getInstance(this).enqueue(reminderRequest);
    }
}