package com.zweckinger.myvaccreg.model.profile;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProfileDao {

    @Insert()
    void insert(Profile profile);

    @Update
    void update(Profile profile);

    @Delete
    void delete(Profile profile);

    @Query("DELETE FROM Profile")
    void deleteAll();

    @Query("SELECT * from Profile ORDER BY profileName ASC")
    LiveData<List<Profile>> getAlphabetizedProfilesLive();

    @Query("SELECT * from Profile ORDER BY profileName ASC")
    List<Profile> getAlphabetizedProfiles();

    @Query("SELECT * from Profile WHERE isActive")
    LiveData<Profile> getActiveProfileLive();

    @Query("SELECT * from Profile WHERE isActive")
    Profile getActiveProfile();
}