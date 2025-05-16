package com.example.jewelryshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ProfilRep {

    private ProfilDAO profilDao;
    private LiveData<Profil> profilLiveData;
    private Context context;

    public ProfilRep(Context context) {
        this.context = context.getApplicationContext();  // Ezzel elkerülöd a memory leak-et
        ProfilRD db = ProfilRD.getInstance(this.context);
        profilDao = db.profilDao();
        profilLiveData = profilDao.getProfil();
    }
    public LiveData<Profil> getProfil() {
        return profilLiveData;
    }

    public void updateProfileImage(String email, Bitmap bitmap, Consumer<Profil> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            String filePath = ImageUtils.saveBitmapToInternalStorage(context, bitmap, email + "_profilkep.png");
            if (filePath == null) {
                // Hiba esetén pl. visszatérhetsz korán, vagy jelezhetsz valamit
                return;
            }


            Profil profil = profilDao.getProfilByEmail(email);
            if (profil == null) {
                profil = new Profil(email, "Név", filePath);
                profilDao.insert(profil);
            } else {
                profil.setProfilKepUri(filePath);
                profilDao.update(profil);
            }

            final Profil profilFinal = profil;

            new Handler(Looper.getMainLooper()).post(() -> callback.accept(profilFinal));
        });
    }
    }




