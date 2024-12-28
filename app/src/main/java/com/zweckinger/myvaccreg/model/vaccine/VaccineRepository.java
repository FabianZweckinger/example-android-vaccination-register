package com.zweckinger.myvaccreg.model.vaccine;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import java.util.List;

public class VaccineRepository {

    private VaccineDao vaccineDao;
    private final LiveData<List<Vaccine>> allVaccines;

    public VaccineRepository(Application application) {
        VeccregDatabase db = VeccregDatabase.getDatabase(application);
        vaccineDao = db.vaccineDao();
        allVaccines = vaccineDao.getAlphabetizedVaccinesLive();
    }

    public void insert(Vaccine vaccine) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            vaccineDao.insert(vaccine);
        });
    }

    public void delete(Vaccine vaccine) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            vaccineDao.delete(vaccine);
        });
    }

    public void update(Vaccine vaccine) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            vaccineDao.update(vaccine);
        });
    }

    public LiveData<List<Vaccine>> getAllVaccines() {
        return allVaccines;
    }
}
