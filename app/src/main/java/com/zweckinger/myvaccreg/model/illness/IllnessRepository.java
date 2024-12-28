package com.zweckinger.myvaccreg.model.illness;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import java.util.List;

public class IllnessRepository {

    private IllnessDao illnessDao;
    private LiveData<List<Illness>> allIllnesses;

    public IllnessRepository(Application application) {
        VeccregDatabase db = VeccregDatabase.getDatabase(application);
        illnessDao = db.illnessDao();
        allIllnesses = illnessDao.getAllAlphabetic();
    }

    public LiveData<List<Illness>> getAllIllnesses() {
        return allIllnesses;
    }

    public void insert(Illness illness) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            illnessDao.insert(illness);
        });
    }

    public void delete(Illness illness) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            illnessDao.delete(illness);
        });
    }

    public void update(Illness illness) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            illnessDao.update(illness);
        });
    }
}
