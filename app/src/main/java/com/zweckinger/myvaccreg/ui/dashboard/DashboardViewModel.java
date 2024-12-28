package com.zweckinger.myvaccreg.ui.dashboard;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.zweckinger.myvaccreg.model.profile.Profile;
import com.zweckinger.myvaccreg.model.profile.ProfileRepository;

public class DashboardViewModel extends AndroidViewModel {

    private final LiveData<Profile> activeProfile;

    public DashboardViewModel(Application application) {
        super(application);
        ProfileRepository profileRepository = new ProfileRepository(application);
        activeProfile = profileRepository.getActiveProfile();
    }

    public LiveData<Profile> getActiveProfile() {
        return activeProfile;
    }
}