package com.example.jewelryshop;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "termekek") //táblaneve
public class Item {

    @PrimaryKey @NonNull @ColumnInfo (name = "nev")
    private String nev;
    @NonNull @ColumnInfo (name = "leiras")
    private String termekleiras;
    @ColumnInfo (name = "tipus")
    private Tipus tipus;
    @NonNull @ColumnInfo (name = "ar")
    private String ar;
    @ColumnInfo (name = "kep")
    private final int kep;


    //konstr
    public Item(@NonNull String nev,@NonNull String termekleiras, Tipus tipus,@NonNull String ar , int kep) {
        this.nev = nev;
        this.termekleiras = termekleiras;
        this.tipus = tipus;

        if (ar == null || ar.trim().isEmpty()) {
            this.ar = "25000";
        } else {
            this.ar = ar;
        }

        if (kep == 0) {
            this.kep = R.drawable.diamondlogo;
        } else {
            this.kep = kep;
        }
    }

    @NonNull
    public String getNev() {
        return nev;
    }

    @NonNull
    public String getTermekleiras() {
        return termekleiras;
    }

    public Tipus getTipus() {
        return tipus;
    }

    @NonNull
    public String getAr() {
        return ar;
    }

    public int getKep() {
        return kep;
    }
}
