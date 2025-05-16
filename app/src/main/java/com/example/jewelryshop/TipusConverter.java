package com.example.jewelryshop;

import androidx.room.TypeConverter;

public class TipusConverter {

    @TypeConverter
    public static String toString(Tipus tipus) {
        return tipus == null ? null : tipus.name();
    }

    @TypeConverter
    public static Tipus toTipus(String tipusString) {
        return tipusString == null ? null : Tipus.valueOf(tipusString);
    }
}
