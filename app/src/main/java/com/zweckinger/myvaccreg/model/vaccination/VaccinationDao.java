package com.zweckinger.myvaccreg.model.vaccination;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Calendar;
import java.util.List;

@Dao
public interface VaccinationDao {

    @Insert()
    void insert(Vaccination vaccination);

    @Update
    void update(Vaccination vaccination);

    @Delete
    void delete(Vaccination vaccination);

    @Query("DELETE FROM Vaccination")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM Vaccination JOIN Profile ON Vaccination.profileId = Profile.profileId WHERE Profile.isActive ORDER BY date DESC")
    LiveData<List<Vaccination>> getAllVaccinationsLive();

    @Transaction
    @Query("SELECT * FROM Vaccination JOIN Profile ON Vaccination.profileId = Profile.profileId WHERE Profile.isActive ORDER BY date DESC")
    List<Vaccination> getAllVaccinations();

    @Query("SELECT * FROM (SELECT * FROM Vaccination ORDER BY date DESC) GROUP BY vaccineId")
    LiveData<List<Vaccination>> getVaccinationsOnlyLatestPerVaccine();

    @Query("SELECT * FROM Vaccination JOIN Profile ON Vaccination.profileId = Profile.profileId WHERE vaccineId=:vaccineId AND dose=:dose AND date=:vaccinationDate")
    Vaccination getVaccination(int vaccineId, int dose, long vaccinationDate);
}