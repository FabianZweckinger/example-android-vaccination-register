package com.zweckinger.myvaccreg.ui.illness;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.zweckinger.myvaccreg.model.illness.Illness;
import com.zweckinger.myvaccreg.model.illness.IllnessRepository;
import com.zweckinger.myvaccreg.ui.MasterViewModel;
import java.io.Serializable;
import java.util.List;

public class IllnessViewModel extends MasterViewModel {

    private final IllnessRepository illnessRepository;

    private final LiveData<List<Illness>> illnesses;

    public IllnessViewModel(Application application) {
        super(application);
        illnessRepository = new IllnessRepository(application);
        illnesses = illnessRepository.getAllIllnesses();
    }

    @Override
    public void insert(Serializable content) {
        illnessRepository.insert((Illness) content);
    }

    @Override
    public void update(Serializable content) {
        illnessRepository.update((Illness) content);
    }

    @Override
    public void delete(Serializable content) {
        illnessRepository.delete((Illness) content);
    }

    LiveData<List<Illness>> getAllIllnesses() {
        return illnesses;
    }
}