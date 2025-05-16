package com.example.jewelryshop;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ItemVM extends AndroidViewModel {
    private ItemRep repository;
    private LiveData<List<Item>> items;

    public ItemVM(Application application) {
        super(application);

        this.repository = new ItemRep(application);
        this.items = repository.getItems();
    }

    public LiveData<List<Item>> getItems() {
        return this.items;
    }

    public void insert(Item word) {
        this.repository.insert(word);
    }
}