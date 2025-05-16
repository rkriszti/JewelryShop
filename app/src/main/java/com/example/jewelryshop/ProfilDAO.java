package com.example.jewelryshop;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProfilDAO {
    @Query("SELECT * FROM profil LIMIT 1")
    LiveData<Profil> getProfil();

    @Query("SELECT * FROM profil LIMIT 1")
    Profil getProfilSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profil profil);

    @Update
    void update(Profil profil);

    @Query("SELECT * FROM profil WHERE email = :email LIMIT 1")
    Profil getProfilByEmail(String email);

    @Query("DELETE FROM profil WHERE email = :email")
    void deleteProfilByEmail(String email);
}
