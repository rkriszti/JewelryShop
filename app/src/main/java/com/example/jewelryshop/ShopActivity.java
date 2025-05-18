package com.example.jewelryshop;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnSuccessListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private ItemVM viewmodel;

    private DrawerLayout drawerLayout;
    private ImageView menuIcon;
    private Spinner spinnerType;




    private RecyclerView recyc;
    private ArrayList<Item> items;
    private ShoppingAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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



        spinnerType = findViewById(R.id.spinnerType);
        ArrayAdapter<String> adapterr = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"nyaklánc", "gyuru", "karkoto"});
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterr);

        // Spinner választás változás
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

// EditText-ek változásának figyelése (TextWatcher) szintén triggerelheti a filterProducts()-t


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
        }else if (id == R.id.menu_cart) {
                Intent intent = new Intent(ShopActivity.this, CartActivity.class);
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

    private void fetchFilteredItems(String minAr, String tipus) {
        db.collection("items")
                .whereGreaterThanOrEqualTo("ar", minAr)
                .whereEqualTo("Tipus", tipus)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        items.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String nev = document.getString("nev");
                            String termekleiras = document.getString("termekleiras");
                            String ar = document.getString("ar");
                            String tipusStr = document.getString("Tipus");
                            Long kep = document.getLong("kep"); // számként tárolod

                            // Átalakítás a saját Item objektumodhoz
                            Tipus tipusEnum = Tipus.valueOf(tipusStr); // Feltételezem enum van

                            int kepResId = (kep != null) ? kep.intValue() : 0;

                            Item item = new Item(nev, termekleiras, tipusEnum, ar, kepResId);
                            items.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Hiba a lekéréskor", e));
    }

    private void filterProducts() {
        String selectedType = spinnerType.getSelectedItem().toString();
        Tipus selectedTipus = mapSpinnerToEnum(selectedType);

        String minPriceStr = ((EditText) findViewById(R.id.minPrice)).getText().toString();
        String maxPriceStr = ((EditText) findViewById(R.id.maxPrice)).getText().toString();

        int minPrice = minPriceStr.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minPriceStr);
        int maxPrice = maxPriceStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxPriceStr);

        List<Item> filteredList = new ArrayList<>();
        for (Item p : items) {
            boolean matchesType = (selectedTipus == null) || p.getTipus() == selectedTipus;

            int price = Integer.parseInt(p.getAr().replaceAll("[^0-9]", ""));
            boolean matchesPrice = price >= minPrice && price <= maxPrice;

            if (matchesType && matchesPrice) {
                filteredList.add(p);
            }
        }

        adapter.setItemList(filteredList);  // a ShoppingAdapter-ben legyen egy setItemList metódus, ami update-eli a listát
    }

    private Tipus mapSpinnerToEnum(String selectedType) {
        switch (selectedType) {
            case "nyaklanc":
                return Tipus.NYAKLANC;  // ezt írd át a valós enum értékre!
            case "gyuru":
                return Tipus.GYURU;
            case "karkoto":
                return Tipus.KARKOTO;
            default:
                return null; // minden típus
        }
    }

    // 1. Lekérdezés: Szűrés tipus alapján, ár szerint rendezve növekvő sorrendben, limitálva az első 5 találatra
    private void queryTypeOrderByPriceLimit() {
        db.collection("items")
                .whereEqualTo("Tipus", "NYAKLANC")         // szűrés tipusra
                .orderBy("ar")                             // rendezés ár szerint (növekvő)
                .limit(5)                                 // legfeljebb 5 elem
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    items.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        addItemFromDocument(document);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error in queryTypeOrderByPriceLimit", e));
    }

    // 2. Lekérdezés: Ár intervallum, tipus szűréssel, ár szerint csökkenő rendezés, léptetéssel (pagination)
    private DocumentSnapshot lastVisibleDoc = null;  // a lapozáshoz

    private void queryPriceRangeWithPagination(int minPrice, int maxPrice, String tipus, boolean nextPage) {
        com.google.firebase.firestore.Query query = db.collection("items")
                .whereGreaterThanOrEqualTo("ar", minPrice)
                .whereLessThanOrEqualTo("ar", maxPrice)
                .whereEqualTo("Tipus", tipus)
                .orderBy("ar", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(5);

        if (nextPage && lastVisibleDoc != null) {
            query = query.startAfter(lastVisibleDoc);
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                lastVisibleDoc = queryDocumentSnapshots.getDocuments()
                        .get(queryDocumentSnapshots.size() - 1);
                items.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    addItemFromDocument(document);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Nincs több elem", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error in queryPriceRangeWithPagination", e));
    }

    // 3. Lekérdezés: Szűrés egy szöveges mező (termekleiras) alapján, rendezés ár szerint növekvő, limitálás
    private void queryDescriptionContainsAndOrderByPrice(String searchTerm) {
        db.collection("items")
                .whereGreaterThanOrEqualTo("termekleiras", searchTerm)
                .whereLessThanOrEqualTo("termekleiras", searchTerm + '\uf8ff')
                .orderBy("termekleiras")
                .orderBy("ar")
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    items.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        addItemFromDocument(document);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error in queryDescriptionContainsAndOrderByPrice", e));
    }

    // Segédfüggvény az Item objektum létrehozásához dokumentumból
    private void addItemFromDocument(QueryDocumentSnapshot document) {
        String nev = document.getString("nev");
        String termekleiras = document.getString("termekleiras");
        String ar = document.getString("ar");
        String tipusStr = document.getString("Tipus");
        Long kep = document.getLong("kep");

        Tipus tipusEnum = Tipus.valueOf(tipusStr);
        int kepResId = (kep != null) ? kep.intValue() : 0;

        Item item = new Item(nev, termekleiras, tipusEnum, ar, kepResId);
        items.add(item);
    }


}
