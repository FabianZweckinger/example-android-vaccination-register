package com.zweckinger.myvaccreg.model.vaccination;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.zweckinger.myvaccreg.model.VeccregDatabase;

import java.util.List;

public class VaccinationRepository {

    private VaccinationDao vaccinationDao;
    private LiveData<List<Vaccination>> allVaccinations;
    private LiveData<List<Vaccination>> vaccinationsOnlyLatestPerVaccine;

    public VaccinationRepository(Application application) {
        VeccregDatabase db = VeccregDatabase.getDatabase(application);
        vaccinationDao = db.vaccinationDao();
        allVaccinations = vaccinationDao.getAllVaccinationsLive();
        vaccinationsOnlyLatestPerVaccine = vaccinationDao.getVaccinationsOnlyLatestPerVaccine();
    }

    public void insert(Vaccination vaccination) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            vaccinationDao.insert(vaccination);
        });
    }

    public void delete(Vaccination vaccination) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            vaccinationDao.delete(vaccination);
        });
    }

    public void update(Vaccination vaccination) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            vaccinationDao.update(vaccination);
        });
    }

    public LiveData<List<Vaccination>> getAllVaccinations() {
        return allVaccinations;
    }

    public LiveData<List<Vaccination>> getVaccinationsOnlyLatestPerVaccine() {
        return vaccinationsOnlyLatestPerVaccine;
    }
}
