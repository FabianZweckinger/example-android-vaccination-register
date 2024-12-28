package com.zweckinger.myvaccreg.model.illness;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import com.zweckinger.myvaccreg.model.IllnessWithVaccines;
import java.util.List;

@Dao
public interface IllnessDao {

    @Insert()
    void insert(Illness illness);

    @Update
    void update(Illness illness);

    @Delete
    void delete(Illness illness);

    @Query("DELETE FROM Illness")
    void deleteAll();

    @Query("SELECT * from Illness ORDER BY illnessName ASC")
    LiveData<List<Illness>> getAllAlphabetic();

    @Query("SELECT * FROM Illness WHERE illnessName = :illnessName")
    Illness getIllness(String illnessName);

    @Transaction
    @Query("SELECT * FROM Illness")
    List<IllnessWithVaccines> getIllnessesWithVaccines();
}