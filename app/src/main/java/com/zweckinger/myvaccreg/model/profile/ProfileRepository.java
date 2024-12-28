package com.zweckinger.myvaccreg.model.profile;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.zweckinger.myvaccreg.model.VeccregDatabase;

import java.util.List;

public class ProfileRepository {

    private final ProfileDao profileDao;
    private final LiveData<List<Profile>> allProfile;
    private final LiveData<Profile> activeProfile;

    public ProfileRepository(Application application) {
        VeccregDatabase db = VeccregDatabase.getDatabase(application);
        profileDao = db.profileDao();
        allProfile = profileDao.getAlphabetizedProfilesLive();
        activeProfile = profileDao.getActiveProfileLive();
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfile;
    }

    public LiveData<Profile> getActiveProfile() {
        return activeProfile;
    }

    public void insert(Profile profile) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            profileDao.insert(profile);
        });
    }

    public void delete(Profile profile) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            profileDao.delete(profile);
        });
    }

    public void update(Profile profile) {
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            profileDao.update(profile);
        });
    }
}
