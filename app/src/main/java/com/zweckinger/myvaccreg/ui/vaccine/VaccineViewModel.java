package com.zweckinger.myvaccreg.ui.vaccine;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.zweckinger.myvaccreg.model.VeccregDatabase;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import com.zweckinger.myvaccreg.model.vaccine.VaccineRepository;
import com.zweckinger.myvaccreg.ui.MasterViewModel;
import java.io.Serializable;
import java.util.List;

public class VaccineViewModel extends MasterViewModel {

    private final VaccineRepository vaccineRepository;
    private final LiveData<List<Vaccine>> vaccines;

    public VaccineViewModel(Application application) {
        super(application);
        vaccineRepository = new VaccineRepository(application);
        vaccines = vaccineRepository.getAllVaccines();
    }

    @Override
    public void insert(Serializable content) {
        vaccineRepository.insert((Vaccine) content);
    }

    @Override
    public void update(Serializable content) {
        vaccineRepository.update((Vaccine) content);
    }

    @Override
    public void delete(Serializable content) {
        vaccineRepository.delete((Vaccine) content);
    }

    public LiveData<List<Vaccine>> getAllVaccines() {
        return vaccines;
    }
}