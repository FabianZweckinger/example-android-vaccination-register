package com.zweckinger.myvaccreg.model.vaccine;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.zweckinger.myvaccreg.model.Converters;
import com.zweckinger.myvaccreg.model.illness.Illness;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@Entity(
        foreignKeys ={
        @ForeignKey(onDelete = CASCADE,
                entity = Illness.class,
                parentColumns = "illnessId",
                childColumns = "illnessId")},
        indices = { @Index("illnessId") })
@TypeConverters(Converters.class)
public class Vaccine implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int vaccineId;

    @NonNull
    private String vaccineName;

    public int illnessId;

    /** Stored in relative time in months */
    private List<Integer> basicImmunizations;

    /** Stored in relative time in months */
    private int boosterVaccinationRepeating;


    public Vaccine(@NonNull String vaccineName, int illnessId, List<Integer> basicImmunizations, int boosterVaccinationRepeating) {
        this.vaccineName = vaccineName;
        this.illnessId = illnessId;
        this.basicImmunizations = basicImmunizations;
        this.boosterVaccinationRepeating = boosterVaccinationRepeating;
    }


    public int getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    @NonNull
    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(@NonNull String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public List<Integer> getBasicImmunizations() {
        return basicImmunizations;
    }

    public void setBasicImmunizations(List<Integer> basicImmunizations) {
        this.basicImmunizations = basicImmunizations;
    }

    public Integer getBoosterVaccinationRepeating() {
        return boosterVaccinationRepeating;
    }

    public void setBoosterVaccinationRepeating(Integer boosterVaccinationRepeating) {
        this.boosterVaccinationRepeating = boosterVaccinationRepeating;
    }

    public int getIllnessId() {
        return illnessId;
    }

    public void setIllnessId(int illnessId) {
        this.illnessId = illnessId;
    }

    @Override
    public String toString() {
        return vaccineName;
    }
}
