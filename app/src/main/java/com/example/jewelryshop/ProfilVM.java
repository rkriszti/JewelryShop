package com.example.jewelryshop;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilVM extends AndroidViewModel {

    private ProfilRep profilRepository;
    private ProfilDAO profilDao;
    private ProfilRD db;
    private ExecutorService executorService;

    // Csak egyszer deklaráljuk MutableLiveData-ként:
    private final MutableLiveData<Profil> profilLiveData = new MutableLiveData<>();

    public ProfilVM(@NonNull Application application) {
        super(application);
        db = ProfilRD.getInstance(application);
        profilDao = db.profilDao();
        profilRepository = new ProfilRep(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    // Csak a LiveData interfészt adjuk ki, így csak olvasható kívülről
    public LiveData<Profil> getProfilLiveData() {
        return profilLiveData;
    }

    public void loadProfil(String email) {
        executorService.execute(() -> {
            Profil profil = profilDao.getProfilByEmail(email);
            profilLiveData.postValue(profil);  // postValue a MutableLiveData-n van, ok

        });
    }

    public void saveProfil(Profil profil) {
        executorService.execute(() -> {
            profilDao.insert(profil);
            profilLiveData.postValue(profil);
        });
    }
}
