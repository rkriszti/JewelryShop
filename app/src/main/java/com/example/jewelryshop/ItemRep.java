package com.example.jewelryshop;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRep {
    private ItemsDAO dao;
    private LiveData<List<Item>> items;

    ItemRep(Application application) {
        ItemRD db = ItemRD.getInstance(application);
        dao = db.itemdao();
        items = dao.getItems();
    }

    public LiveData<List<Item>> getItems() {
        return items;
    }

    public void insert(Item item) {
        new Insert(this.dao).execute(item);
    }


    private static class Insert extends AsyncTask<Item, Void, Void> {
        private ItemsDAO mDAO;

        Insert(ItemsDAO dao) {
            this.mDAO = dao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            mDAO.insert(items[0]);
            return null;
        }
    }
}