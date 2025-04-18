package com.example.jewelryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.navigation.NavigationView;

public class ShopActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu);

        // Menü gomb működésének beállítása
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ha a menü gombra kattintunk, nyisd meg a navigációs menüt
                drawerLayout.openDrawer(findViewById(R.id.navigation_view));
            }
        });

        // A navigációs menü kezelése
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.menu_logout) {
                    // Kijelentkezés
                    FirebaseAuth.getInstance().signOut(); // Kijelentkeztetjük a felhasználót
                    Toast.makeText(ShopActivity.this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();

                    // Átirányítás a belépési oldalra
                    Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Bezárja az összes többi activity-t
                    startActivity(intent);
                    finish(); // Bezárja az aktuális activity-t
                    return true;
                }
                return false; // Ha másik menüelemre kattintunk, ne tegyünk semmit
            }
        });
    }
}
