package com.example.jewelryshop;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "profil")
public class Profil {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "nev")
    private String nev;

    @ColumnInfo(name = "profil_kep_uri")
    private String profilKepUri;

    public Profil(@NonNull String email, String nev, String profilKepUri) {
        this.email = email;
        this.nev = nev;
        this.profilKepUri = profilKepUri;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getProfilKepUri() {
        return profilKepUri;
    }

    public void setProfilKepUri(String profilKepUri) {
        this.profilKepUri = profilKepUri;
    }
}
