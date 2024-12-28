package com.zweckinger.myvaccreg.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.zweckinger.myvaccreg.model.vaccination.Vaccination;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;

import java.util.List;

public class VaccineWithVaccinations {
    @Embedded
    public Vaccine vaccine;
    @Relation(
            parentColumn = "vaccineId",
            entityColumn = "vaccinationId"
    )
    public List<Vaccination> vaccinations;
}