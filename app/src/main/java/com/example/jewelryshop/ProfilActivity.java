package com.example.jewelryshop;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilActivity extends AppCompatActivity {

    private static final String TAG = "ProfilActivity";

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1001;

    private ImageView profileImageView;
    private Button buttonSelectImage;
    private ProfilVM profilViewModel;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;  // <<< ide kell az inicializálás

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Activity létrejött");
        setContentView(R.layout.activity_profil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileImageView = findViewById(R.id.profileImageView);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu);

        profilViewModel = new ViewModelProvider(this).get(ProfilVM.class);

        Log.d(TAG, "onCreate: pickImageLauncher regisztrálása");
        // Kép választó launcher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    Log.d(TAG, "pickImageLauncher callback, uri: " + uri);
                    if (uri != null) {
                        Log.d(TAG, "pickImageLauncher: Uri nem null, kép betöltése");
                        profilViewModel.loadProfil(uri.toString());

                        Glide.with(this)
                                .load(uri)
                                .circleCrop()
                                .placeholder(R.drawable.usericon)
                                .into(profileImageView);

                        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        prefs.edit().putString("profile_image_uri", uri.toString()).apply();
                    } else {
                        Log.d(TAG, "pickImageLauncher: Uri null, nem választott képet");
                    }
                }
        );

        Log.d(TAG, "onCreate: requestPermissionLauncher regisztrálása");
        // Engedélykérés launcher - EZ HIÁNYZOTT
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    Log.d(TAG, "requestPermissionLauncher callback, isGranted: " + isGranted);
                    if (isGranted) {
                        Toast.makeText(this, "Engedély megadva, most már választhatsz képet!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Engedély megadva, indítom a pickImageLauncher-t");
                        pickImageLauncher.launch("image/*");
                    } else {
                        Toast.makeText(this, "Engedély nélkül nem tudsz képet választani.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Engedély megtagadva");
                    }
                }
        );

        menuIcon.setOnClickListener(v -> {
            Log.d(TAG, "menuIcon kattintás: nyitom a drawer-t");
            drawerLayout.openDrawer(findViewById(R.id.navigation_view));
        });

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            Log.d(TAG, "navigationView elem kiválasztva, id: " + item.getItemId());
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
                Log.d(TAG, "Profil menü kiválasztva");
                Intent intent = new Intent(ProfilActivity.this, ProfilActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.menu_logout) {
                Log.d(TAG, "Kijelentkezés kiválasztva");
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfilActivity.this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.menu_shop) {
                Log.d(TAG, "Shop menü kiválasztva");
                Intent intent = new Intent(ProfilActivity.this, ShopActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });

        profilViewModel.getProfilLiveData().observe(this, profil -> {
            Log.d(TAG, "profilViewModel LiveData változás: " + profil);
            if (profil != null && profil.getProfilKepUri() != null) {
                Glide.with(this)
                        .load(Uri.parse(profil.getProfilKepUri()))
                        .circleCrop()
                        .placeholder(R.drawable.usericon)
                        .into(profileImageView);
            }
        });

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String imageUriString = prefs.getString("profile_image_uri", null);

        if (imageUriString != null) {
            Log.d(TAG, "SharedPreferences-ből betöltött kép URI: " + imageUriString);
            Uri imageUri = Uri.parse(imageUriString);
            Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .placeholder(R.drawable.usericon)
                    .into(profileImageView);
        } else {
            Log.d(TAG, "Nincs elmentett kép URI, alapértelmezett ikon beállítása");
            profileImageView.setImageResource(R.drawable.usericon);
        }

        buttonSelectImage.setOnClickListener(v -> {
            Log.d(TAG, "buttonSelectImage kattintás");
            if (hasStoragePermission()) {
                Log.d(TAG, "Engedély már megvan, indítom a képválasztót");
                pickImageLauncher.launch("image/*");
            } else {
                Log.d(TAG, "Nincs engedély, engedélykérés indítása");
                requestPermissionLauncher.launch(
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                                Manifest.permission.READ_MEDIA_IMAGES :
                                Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            Log.d(TAG, "hasStoragePermission: READ_MEDIA_IMAGES engedély = " + granted);
            return granted;
        } else {
            boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            Log.d(TAG, "hasStoragePermission: READ_EXTERNAL_STORAGE engedély = " + granted);
            return granted;
        }
    }

    // Ezek már nem szükségesek az ActivityResultLauncher-rel, de bent hagyhatod, ha akarod
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult hívás, requestCode = " + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: Engedély megadva");
                Toast.makeText(this, "Engedély megadva, most már választhatsz képet!", Toast.LENGTH_SHORT).show();
                pickImageLauncher.launch("image/*");
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Engedély megtagadva");
                Toast.makeText(this, "Engedély nélkül nem tudsz képet választani.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
