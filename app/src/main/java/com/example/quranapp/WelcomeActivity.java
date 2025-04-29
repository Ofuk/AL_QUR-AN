// File: java/com/example/quranapp/WelcomeActivity.java
package com.example.quranapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.quranapp.fragment.BookmarkFragment;
import com.example.quranapp.fragment.JuzFragment;
import com.example.quranapp.fragment.SurahFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;
    private TextView tvWelcome;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnReadQuran, btnSettings, btnProfile, btnLogout, btnLogin;
    private boolean isTabsVisible = false; // Untuk melacak apakah tab sudah ditampilkan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Terapkan tema dari pengaturan
        SharedPreferences sharedPreferences = getSharedPreferences("QuranAppPrefs", MODE_PRIVATE);
        boolean darkMode = sharedPreferences.getBoolean("darkMode", false);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Inisialisasi Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Inisialisasi UI
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnReadQuran = findViewById(R.id.btnReadQuran);
        btnSettings = findViewById(R.id.btnSettings);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogin = findViewById(R.id.btnLogin);

        // Konfigurasi Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Cek apakah pengguna sudah login
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Pengguna sudah login: " + currentUser.getEmail());
            updateUI(true);
        } else {
            Log.d(TAG, "Pengguna belum login");
            updateUI(false);
        }

        // Inisialisasi ActivityResultLauncher untuk login
        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d(TAG, "Hasil login diterima, kode: " + result.getResultCode());
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data == null) {
                    Log.e(TAG, "Data intent kosong setelah login");
                    android.widget.Toast.makeText(this, "Gagal mendapatkan data login", android.widget.Toast.LENGTH_LONG).show();
                    return;
                }
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "Google Sign-In berhasil, email: " + account.getEmail());
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    Log.e(TAG, "Google Sign-In gagal, kode: " + e.getStatusCode(), e);
                    android.widget.Toast.makeText(this, "Google Sign-In gagal: " + e.getMessage() + " (kode: " + e.getStatusCode() + ")", android.widget.Toast.LENGTH_LONG).show();
                }
            } else {
                String message = "Login dibatalkan atau gagal";
                if (result.getResultCode() == RESULT_CANCELED) {
                    message = "Login dibatalkan oleh pengguna";
                }
                Log.w(TAG, message + ", kode: " + result.getResultCode());
                android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        // Listener untuk tombol
        btnReadQuran.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // File: java/com/example/quranapp/WelcomeActivity.java
// ... kode lainnya tetap sama ...

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    android.widget.Toast.makeText(this, "Please login first", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(WelcomeActivity.this, ProfileActivity.class);
                intent.putExtra("user_name", user.getDisplayName());
                intent.putExtra("user_email", user.getEmail());
                // Tambahkan URL foto profil jika tersedia
                intent.putExtra("user_photo_url", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);
                startActivity(intent);
            });
        }

// ... kode lainnya tetap sama ...

        btnLogout.setOnClickListener(v -> signOut());

        btnLogin.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Log.d(TAG, "Memulai proses login");
        // Periksa apakah Google Play Services tersedia
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (availability == ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play Services tersedia, meluncurkan intent login");
            Intent signInIntent = googleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        } else {
            Log.e(TAG, "Google Play Services tidak tersedia, kode: " + availability);
            android.widget.Toast.makeText(this, "Google Play Services tidak tersedia. Silakan perbarui.", android.widget.Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "Mengautentikasi dengan Firebase");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Autentikasi Firebase berhasil");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Pengguna: " + user.getEmail());
                        }
                        android.widget.Toast.makeText(this, "Anda berhasil login", android.widget.Toast.LENGTH_SHORT).show();
                        updateUI(true);
                    } else {
                        Log.e(TAG, "Autentikasi Firebase gagal", task.getException());
                        android.widget.Toast.makeText(this, "Autentikasi gagal: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), android.widget.Toast.LENGTH_LONG).show();
                        updateUI(false);
                    }
                });
    }

    private void signOut() {
        Log.d(TAG, "Logout pengguna");
        firebaseAuth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Log.d(TAG, "Logout selesai");
            updateUI(false);
            isTabsVisible = false; // Reset status tab saat logout
            android.widget.Toast.makeText(this, "Logged out successfully", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUI(boolean isLoggedIn) {
        Log.d(TAG, "Memperbarui UI, isLoggedIn: " + isLoggedIn);
        if (isLoggedIn) {
            btnLogin.setVisibility(View.GONE);
            btnReadQuran.setVisibility(View.VISIBLE);
            btnSettings.setVisibility(View.VISIBLE);
            btnProfile.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);

            // Tampilkan tab hanya jika sudah diklik sebelumnya
            if (isTabsVisible) {
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                btnReadQuran.setVisibility(View.GONE);
                if (viewPager.getAdapter() == null) {
                    setupViewPager();
                }
            } else {
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
            }
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            btnReadQuran.setVisibility(View.GONE);
            btnSettings.setVisibility(View.GONE);
            btnProfile.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void setupViewPager() {
        Log.d(TAG, "Mengatur ViewPager");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SurahFragment(), "Surah");
        adapter.addFragment(new JuzFragment(), "Juz");
        adapter.addFragment(new BookmarkFragment(), "Bookmark");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Adapter untuk ViewPager
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}