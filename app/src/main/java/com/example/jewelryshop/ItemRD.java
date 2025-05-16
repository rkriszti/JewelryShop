package com.example.jewelryshop;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
@Database(entities = {Item.class}, version = 1, exportSchema = false)
@TypeConverters({TipusConverter.class})

public abstract class ItemRD extends RoomDatabase {

    public abstract ItemsDAO itemdao();
    private static ItemRD instance; //obj példány

    public static ItemRD getInstance(Context context) {
        if (instance == null) {
            synchronized (ItemRD.class) { //lock-olom az osztályt
                if (instance == null) {
                    //létrehoz ha nincs
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(), ItemRD.class,
                                    "termekek").fallbackToDestructiveMigration()
                            .addCallback(populationCallback).build();
                }
            }
        }

        return instance;
    }

    private static RoomDatabase.Callback populationCallback = new RoomDatabase.Callback() {
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            new InitDb(instance).execute();
        }
    };

    private static class InitDb extends AsyncTask<Void, Void, Void> {
        private ItemsDAO dao;

        InitDb(ItemRD db) {
            this.dao = db.itemdao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();

            dao.insert(new Item("Arany gyűrű", "Kézzel készült arany gyűrű", Tipus.GYURU, "35000", R.drawable.diamondlogo));
            dao.insert(new Item("Ezüst nyaklánc", "Elegáns nyaklánc medállal", Tipus.NYAKLANC, "", 0)); // Default ár és kép
            dao.insert(new Item("Modern fülbevaló", "Divatos ezüst fülbevaló", Tipus.FULBEVALO, "15000", R.drawable.diamondlogo));

            return null;
        }
    }

}