package com.zweckinger.myvaccreg.ui.vaccination;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zweckinger.myvaccreg.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class VaccinationViewHolder extends RecyclerView.ViewHolder {

    private final TextView vaccineItemView;
    private final TextView doseItemView;
    private final TextView dateItemView;
    private final TextView nextVaccTypeItemView;
    private final TextView nextVaccDateItemView;

    public VaccinationViewHolder(@NonNull View itemView) {
        super(itemView);
        vaccineItemView = itemView.findViewById(R.id.item_vaccination_vaccine_name_protection_textView);
        doseItemView = itemView.findViewById(R.id.item_vaccination_dose_textView);
        dateItemView = itemView.findViewById(R.id.item_vaccination_date_textView);
        nextVaccTypeItemView = itemView.findViewById(R.id.item_vaccination_next_vacctype_textView);
        nextVaccDateItemView = itemView.findViewById(R.id.item_vaccination_next_vaccdate_textView);
    }

    public void bind(String vaccineName, String doseText, String dateString, String illnessName, String nextVaccType, String nextVaccDateString) {
        vaccineItemView.setText(vaccineName + " (" + illnessName + ")");
        doseItemView.setText(doseText);
        dateItemView.setText(dateString);
        nextVaccTypeItemView.setText(nextVaccType);
        nextVaccDateItemView.setText(nextVaccDateString);
    }

    static VaccinationViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vaccination, parent, false);
        return new VaccinationViewHolder(view);
    }
}