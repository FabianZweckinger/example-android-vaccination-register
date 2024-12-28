package com.zweckinger.myvaccreg.model.vaccination;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.zweckinger.myvaccreg.model.Converters;
import com.zweckinger.myvaccreg.model.profile.Profile;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;

import java.io.Serializable;
import java.util.Calendar;

@Entity(
        foreignKeys =
                {
                        @ForeignKey(onDelete = CASCADE,
                                entity = Vaccine.class,
                                parentColumns = "vaccineId",
                                childColumns = "vaccineId"),
                        @ForeignKey(onDelete = CASCADE,
                                entity = Profile.class,
                                parentColumns = "profileId",
                                childColumns = "profileId")
                },
        indices = {
                @Index("vaccineId"),
                @Index("profileId")
        })
@TypeConverters(Converters.class)
public class Vaccination implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int vaccinationId;

    private int vaccineId;

    private int profileId;

    private int dose;

    private Calendar date;

    public Vaccination(int profileId, int vaccineId, int dose, Calendar date) {
        this.profileId = profileId;
        this.vaccineId = vaccineId;
        this.dose = dose;
        this.date = date;
    }

    public int getVaccinationId() {
        return vaccinationId;
    }

    public void setVaccinationId(int vaccinationId) {
        this.vaccinationId = vaccinationId;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(int vaccineId) {
        this.vaccineId = vaccineId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
}
