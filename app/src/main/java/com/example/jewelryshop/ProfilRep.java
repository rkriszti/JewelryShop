package com.example.jewelryshop;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

public class ProfilRep {

    private ProfilDAO profilDao;
    private LiveData<Profil> profilLiveData;

    public ProfilRep(Context context) {
        ProfilRD db = ProfilRD.getInstance(context);
        profilDao = db.profilDao();
        profilLiveData = profilDao.getProfil();  // Egy profil van tárolva
    }

    public LiveData<Profil> getProfil() {
        return profilLiveData;
    }

    public void updateProfileImage(String uri) {
        AsyncTask.execute(() -> {
            Profil profil = profilDao.getProfilSync();
            if (profil == null) {
                profil = new Profil("user@example.com", "Név", uri);
                profilDao.insert(profil);
            } else {
                profil.setProfilKepUri(uri);
                profilDao.update(profil);
            }
        });
    }
}
