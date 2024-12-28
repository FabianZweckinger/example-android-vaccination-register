package com.zweckinger.myvaccreg.model.illness;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class Illness implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int illnessId;

    @NonNull
    private String illnessName;

    public Illness(@NonNull String illnessName) {
        this.illnessName = illnessName;
    }

    @NonNull
    public String getIllnessName() {
        return illnessName;
    }

    public void setIllnessName(@NonNull String illnessName) {
        this.illnessName = illnessName;
    }

    public int getIllnessId() {
        return illnessId;
    }

    public void setIllnessId(int illnessId) {
        this.illnessId = illnessId;
    }
}
