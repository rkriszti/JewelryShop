package com.example.jewelryshop;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Profil.class}, version = 1, exportSchema = false)
public abstract class ProfilRD extends RoomDatabase {

    private static volatile ProfilRD INSTANCE;

    public abstract ProfilDAO profilDao();

    public static ProfilRD getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ProfilRD.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ProfilRD.class, "profil_database")
                            .fallbackToDestructiveMigration() // Egyszerű migráció helyett törli az adatot, ha változik az adatbázis szerkezete
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
