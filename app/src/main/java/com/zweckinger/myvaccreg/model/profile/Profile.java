package com.zweckinger.myvaccreg.model.profile;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class Profile implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int profileId;

    @NonNull
    private String profileName;

    private boolean isActive = false;

    public Profile(@NonNull String profileName) {
        this.profileName = profileName;
    }

    @NonNull
    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(@NonNull String profileName) {
        this.profileName = profileName;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
