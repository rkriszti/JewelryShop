package com.example.jewelryshop;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemsDAO {

    @Insert
    void insert(Item item);

    @Query("DELETE FROM termekek")
    void deleteAll();

    //@Delete
    // void delete(Word word);

    //kilistáz mindent
    @Query("SELECT * FROM termekek ORDER BY nev ASC")
    LiveData<List<Item>> getItems(); //livedata hogy tudjunk a változásról

}