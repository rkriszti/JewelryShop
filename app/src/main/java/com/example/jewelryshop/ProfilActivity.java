package com.example.jewelryshop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.widget.Button;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import androidx.core.app.NotificationCompat;
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

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private Button buttonSelectImage;
    private Button buttonTakePhoto;
    private ProfilVM profilViewModel;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ActivityResultLauncher<String> requestGalleryPermissionLauncher;

    private ActivityResultLauncher<Void> takePictureLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private String currentEmail;
    private Button buttonDeleteProfile;

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
            finish();
            return;
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
                // Toast.makeText(ShopActivity.this, "Profil oldalra irányít", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfilActivity.this, ProfilActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfilActivity.this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            }else if (id == R.id.menu_shop) {
                Intent intent = new Intent(ProfilActivity.this, ShopActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }else if (id == R.id.menu_cart) {
                Intent intent = new Intent(ProfilActivity.this, CartActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });
        buttonDeleteProfile = findViewById(R.id.buttonDeleteProfile);

        buttonDeleteProfile.setOnClickListener(v -> {
            deleteUserProfile();
        });


        // Nézetek inicializálása
        profileImageView = findViewById(R.id.profileImageView);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu);
        profilViewModel = new ViewModelProvider(this).get(ProfilVM.class);
        profilViewModel.loadProfil(currentEmail);

        // Galériából kép választása
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

        // Kamera kép készítés launcher (kép visszaadása bitmapként)
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        profilViewModel.updateProfileImage(currentEmail, bitmap);
                        Toast.makeText(this, "Kép elkészült a kamerával.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Nem készült el a kép.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Galéria engedély kérő
        requestGalleryPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        pickImageLauncher.launch("image/*");
                    } else {
                        Toast.makeText(this, "Engedély megtagadva, nem tudsz képet választani.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Kamera engedély kérő
        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        takePictureLauncher.launch(null);
                    } else {
                        Toast.makeText(this, "Engedély megtagadva, nem tudsz képet készíteni.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Profilkép betöltése Glide-dal vagy alapértelmezett kép
        profilViewModel.getProfilLiveData().observe(this, profil -> {
            if (profil != null && profil.getProfilKepUri() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(profil.getProfilKepUri());
                profileImageView.setImageBitmap(bitmap);
            } else {
                profileImageView.setImageResource(R.drawable.usericon);
            }
        });

        // Menü ikon eseménykezelése
        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.navigation_view)));

        // Navigációs menü kezelése (ugyanaz maradhat)

        // Gomb események kezelése

        // Galéria gomb - engedély ellenőrzés + kérés
        buttonSelectImage.setOnClickListener(v -> {
            String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                    Manifest.permission.READ_MEDIA_IMAGES :
                    Manifest.permission.READ_EXTERNAL_STORAGE;

            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                pickImageLauncher.launch("image/*");
            } else {
                requestGalleryPermissionLauncher.launch(permission);
            }
        });

        // Kamera gomb - engedély ellenőrzés + kérés
        buttonTakePhoto.setOnClickListener(v -> {
            String permission = Manifest.permission.CAMERA;

            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                takePictureLauncher.launch(null);
            } else {
                requestCameraPermissionLauncher.launch(permission);
            }
        });



    }
    private void deleteUserProfile() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfilActivity.this, "Profil törölve.", Toast.LENGTH_SHORT).show();
                            // Vissza a bejelentkező képernyőre
                            Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ProfilActivity.this, "Sikertelen törlés: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Nincs bejelentkezett felhasználó.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        profilViewModel.loadProfil(currentEmail);
        showNotification();
        startAlarm();


    }

    private void showNotification() {
        String channelId = "profil_channel";
        String channelName = "Profil értesítések";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.usericon)
                .setContentTitle("Profil")
                .setContentText("Üdv a profilodban!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
    private void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Ismétlődő alarm 1 percenként (60000 ms)
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 60000,
                60000,
                pendingIntent);
    }
}
