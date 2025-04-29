// File: java/com/example/quranapp/ProfileActivity.java
package com.example.quranapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private ImageView ivProfilePicture;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi UI
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);

        // Ambil data dari Intent
        String userName = getIntent().getStringExtra("user_name");
        String userEmail = getIntent().getStringExtra("user_email");
        String userPhotoUrl = getIntent().getStringExtra("user_photo_url");

        // Tampilkan data, atau pesan default jika data tidak tersedia
        tvName.setText(userName != null ? userName : "Nama tidak tersedia");
        tvEmail.setText(userEmail != null ? userEmail : "Email tidak tersedia");

        // Muat gambar profil menggunakan Glide
        if (userPhotoUrl != null && !userPhotoUrl.isEmpty()) {
            Glide.with(this)
                    .load(userPhotoUrl)
                    .apply(RequestOptions.circleCropTransform()) // Membuat gambar menjadi lingkaran
                    .placeholder(R.drawable.ic_placeholder_profile) // Gambar placeholder jika gagal memuat
                    .error(R.drawable.ic_error_profile) // Gambar error jika gagal memuat
                    .into(ivProfilePicture);
        } else {
            // Jika URL tidak tersedia, gunakan gambar default
            ivProfilePicture.setImageResource(R.drawable.ic_placeholder_profile);
        }
    }
}