package com.zweckinger.myvaccreg.model.vaccine;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.zweckinger.myvaccreg.model.VaccineWithVaccinations;
import com.zweckinger.myvaccreg.model.illness.Illness;

import java.util.List;

@Dao
public interface VaccineDao {

    @Insert()
    void insert(Vaccine vaccine);

    @Update
    void update(Vaccine vaccine);

    @Delete
    void delete(Vaccine vaccine);

    @Query("DELETE FROM Vaccine")
    void deleteAll();

    @Query("SELECT * from Vaccine ORDER BY vaccineName ASC")
    LiveData<List<Vaccine>> getAlphabetizedVaccinesLive();

    @Query("SELECT * from Vaccine ORDER BY vaccineName ASC")
    List<Vaccine> getAlphabetizedVaccines();

    @Query("SELECT * from Vaccine WHERE vaccineName = :vaccineName")
    Vaccine getVaccine(String vaccineName);

    @Query("SELECT * from Vaccine WHERE vaccineId = :vaccineId")
    Vaccine getVaccine(int vaccineId);

    @Query("SELECT * FROM Vaccine WHERE illnessId = :illnessId")
    List<Vaccine> getVaccinesForIllness(int illnessId);

    @Transaction
    @Query("SELECT * FROM Vaccine")
    List<VaccineWithVaccinations> getVaccinesWithVaccinations();

    @Query("SELECT * FROM Illness JOIN Vaccine ON Illness.illnessId = Vaccine.illnessId WHERE vaccineId = :vaccineId")
    Illness getIllness(int vaccineId);
}