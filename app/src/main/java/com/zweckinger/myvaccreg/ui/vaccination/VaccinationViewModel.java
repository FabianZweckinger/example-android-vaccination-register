package com.zweckinger.myvaccreg.ui.vaccination;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.zweckinger.myvaccreg.model.vaccination.Vaccination;
import com.zweckinger.myvaccreg.model.vaccination.VaccinationRepository;
import com.zweckinger.myvaccreg.ui.MasterViewModel;

import java.io.Serializable;
import java.util.List;

public class VaccinationViewModel extends MasterViewModel {

    private final VaccinationRepository vaccinationRepository;

    /** All vaccination for the current user. */
    private final LiveData<List<Vaccination>> allVaccinations;

    /** Vaccinations for the current user, only with only the latest vaccination per vaccine. */
    private final LiveData<List<Vaccination>> vaccinationsOnlyLatestPerVaccine;

    public VaccinationViewModel(Application application) {
        super(application);
        vaccinationRepository = new VaccinationRepository(application);
        allVaccinations = vaccinationRepository.getAllVaccinations();
        vaccinationsOnlyLatestPerVaccine = vaccinationRepository.getVaccinationsOnlyLatestPerVaccine();
    }

    @Override
    public void insert(Serializable content) {
        vaccinationRepository.insert((Vaccination) content);
    }

    @Override
    public void update(Serializable content) {
        vaccinationRepository.update((Vaccination) content);
    }

    @Override
    public void delete(Serializable content) {
        vaccinationRepository.delete((Vaccination) content);
    }

    LiveData<List<Vaccination>> getAllVaccinations() {
        return allVaccinations;
    }

    public LiveData<List<Vaccination>> getVaccinationsOnlyLatestPerVaccine() {
        return vaccinationsOnlyLatestPerVaccine;
    }
}