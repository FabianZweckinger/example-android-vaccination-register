package com.zweckinger.myvaccreg.ui.profile;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.zweckinger.myvaccreg.model.profile.Profile;
import com.zweckinger.myvaccreg.model.profile.ProfileRepository;
import com.zweckinger.myvaccreg.ui.MasterViewModel;
import java.io.Serializable;
import java.util.List;

public class ProfileViewModel extends MasterViewModel {

    private final ProfileRepository profileRepository;

    private final LiveData<List<Profile>> profiles;

    public ProfileViewModel(Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
        profiles = profileRepository.getAllProfiles();
    }

    @Override
    public void insert(Serializable content) {
        profileRepository.insert((Profile) content);
    }

    @Override
    public void update(Serializable content) {
        profileRepository.update((Profile) content);
    }

    @Override
    public void delete(Serializable content) {
        profileRepository.delete((Profile) content);
    }

    LiveData<List<Profile>> getAllProfiles() {
        return profiles;
    }
}