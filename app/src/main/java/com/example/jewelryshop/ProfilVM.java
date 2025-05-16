package com.example.jewelryshop;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilVM extends AndroidViewModel {

    private ProfilRep profilRepository;
    private ProfilDAO profilDao;
    private ProfilRD db;
    private ExecutorService executorService;

    private final MutableLiveData<Profil> profilLiveData = new MutableLiveData<>();

    public ProfilVM(@NonNull Application application) {
        super(application);
        db = ProfilRD.getInstance(application);
        profilDao = db.profilDao();
        profilRepository = new ProfilRep(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Profil> getProfilLiveData() {
        return profilLiveData;
    }

    public void loadProfil(String email) {
        executorService.execute(() -> {
            Profil profil = profilDao.getProfilByEmail(email);
            Log.d("ProfilVM", "Betöltött profilKepUri: " + (profil != null ? profil.getProfilKepUri() : "null"));
            profilLiveData.postValue(profil);
        });
    }

    public void saveProfil(Profil profil) {
        executorService.execute(() -> {
            profilDao.insert(profil);
            profilLiveData.postValue(profil);
        });
    }

    public void updateProfileImage(String email, Bitmap bitmap) {
        executorService.execute(() -> {
            File imageFile = new File(getApplication().getFilesDir(), email + "_profile.png");
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Profil profil = profilDao.getProfilByEmail(email);
            if (profil == null) {
                profil = new Profil(email, "", imageFile.getAbsolutePath());  // üres névvel
                profilDao.insert(profil);
            } else {
                profil.setProfilKepUri(imageFile.getAbsolutePath());
                profilDao.update(profil);
            }

            profilLiveData.postValue(profil);

            Log.d("ProfilVM", "Frissített profilKepUri: " + profil.getProfilKepUri());
        });
    }


}
