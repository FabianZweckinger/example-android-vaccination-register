package com.zweckinger.myvaccreg.model;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.zweckinger.myvaccreg.model.illness.Illness;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import java.util.List;

public class IllnessWithVaccines {
    @Embedded
    public Illness illness;
    @Relation(
            parentColumn = "illnessId",
            entityColumn = "vaccineId"
    )
    public List<Vaccine> vaccines;
}
