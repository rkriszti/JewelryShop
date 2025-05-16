package com.example.jewelryshop;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private ItemVM viewmodel;

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;

    private RecyclerView recyc;
    private ArrayList<Item> items;
    private ShoppingAdapter adapter;
    private int gridNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuIcon = findViewById(R.id.menu);

        menuIcon.setOnClickListener(v ->
                drawerLayout.openDrawer(findViewById(R.id.navigation_view))
        );

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
               // Toast.makeText(ShopActivity.this, "Profil oldalra irányít", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShopActivity.this, ProfilActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ShopActivity.this, "Sikeres kijelentkezés", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            }else if (id == R.id.menu_shop) {
                Intent intent = new Intent(ShopActivity.this, ShopActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }
            return false;
        });

        items = new ArrayList<>();
        adapter = new ShoppingAdapter(this, items);
        recyc = findViewById(R.id.recycle);
        recyc.setLayoutManager(new GridLayoutManager(this, gridNumber));

        recyc.setAdapter(adapter);

        viewmodel = new ViewModelProvider(this).get(ItemVM.class);
        viewmodel.getItems().observe(this, items -> {
            adapter.setItemList(items);
        });
    }

    // Menü betöltése (ha van egyéb opciós menü)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    // Opciós menüelemek kezelése (pl. toolbar menü)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_cart) {
            Toast.makeText(this, "Kosár menü kattintva", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeData() {
        String[] itemss = {"Arany gyűrű", "Ezüst lánc", "Gyémánt fülbevaló"};
        String[] termekleiras = {"Szép gyűrű", "Elegáns lánc", "Csillogó fülbevaló"};
        String[] ar = {"15000 Ft", "20000 Ft", "30000 Ft"};
        Tipus[] tipus = {Tipus.GYURU, Tipus.NYAKLANC, Tipus.FULBEVALO};
        TypedArray kep = getResources().obtainTypedArray(R.array.image_array);

        items.clear();
        for (int i = 0; i < itemss.length; i++) {
            items.add(new Item(itemss[i], termekleiras[i], tipus[i], ar[i],  kep.getResourceId(i, 0)));
        }

        kep.recycle();
        adapter.notifyDataSetChanged();
    }

    public void updateAlertIcon() {
    }
}
