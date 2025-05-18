package com.example.jewelryshop;

import android.content.Intent;import
        androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CartActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;  // hozzáadva!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        drawerLayout = findViewById(R.id.drawer_layout);  // győződj meg róla, hogy van ilyen ID az XML-ben!

        ImageView menuIcon = findViewById(R.id.menu);
        menuIcon.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });


        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
                Intent intent = new Intent(CartActivity.this, ProfilActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(CartActivity.this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.menu_shop) {
                Intent intent = new Intent(CartActivity.this, ShopActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.menu_cart) {
                // Már itt vagyunk, zárd csak be a menüt
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });
    }
}
