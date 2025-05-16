package com.example.jewelryshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private Button buttonSelectImage;
    private ProfilVM profilViewModel;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Toolbar beállítása
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Firebase bejelentkezett felhasználó email lekérdezése
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentEmail = (auth.getCurrentUser() != null) ? auth.getCurrentUser().getEmail() : null;
        if (currentEmail == null) {
            Toast.makeText(this, "Nincs bejelentkezett felhasználó.", Toast.LENGTH_SHORT).show();
            finish(); // vagy más logika
            return;
        }


        // Nézetek inicializálása
        profileImageView = findViewById(R.id.profileImageView);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu);
        profilViewModel = new ViewModelProvider(this).get(ProfilVM.class);
        profilViewModel.loadProfil(currentEmail);

        // Kép kiválasztása
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            profilViewModel.updateProfileImage(currentEmail, bitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, "Nem sikerült betölteni a képet.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
        );

        // Jogosultság kérő launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "Engedély megadva, most már választhatsz képet!", Toast.LENGTH_SHORT).show();
                        pickImageLauncher.launch("image/*");
                    } else {
                        Toast.makeText(this, "Engedély megtagadva, nem tudsz képet választani.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        ImageView profileImageView;
        profileImageView = findViewById(R.id.profileImageView);
        // Profilkép betöltése Glide-dal
        profilViewModel.getProfilLiveData().observe(this, profil -> {
            if (profil != null && profil.getProfilKepUri() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(profil.getProfilKepUri());
                profileImageView.setImageBitmap(bitmap);
            } else {
                // alapértelmezett kép, ha nincs profilkép
                profileImageView.setImageResource(R.drawable.usericon);
            }
        });


        // Menü ikon eseménykezelése
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.navigation_view)));

        // Navigációs menü
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.menu_profile) {
                intent = new Intent(this, ProfilActivity.class);
            } else if (id == R.id.menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.menu_shop) {
                intent = new Intent(this, ShopActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }

            return false;
        });

        // Gomb eseménykezelő: engedély kérése és képválasztás
        buttonSelectImage.setOnClickListener(v -> {
            String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                    Manifest.permission.READ_MEDIA_IMAGES :
                    Manifest.permission.READ_EXTERNAL_STORAGE;
            requestPermissionLauncher.launch(permission);
        });
    }
}
