package com.zweckinger.myvaccreg.ui.vaccine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;

import java.util.List;

public class VaccineListAdapter extends ArrayAdapter<Vaccine> {

    private final ActivityResultLauncher<Intent> vaccineActivityResultLauncher;

    public VaccineListAdapter(Context context, List<Vaccine> vaccines, ActivityResultLauncher<Intent> vaccineActivityResultLauncher) {
        super(context, 0, vaccines);
        this.vaccineActivityResultLauncher = vaccineActivityResultLauncher;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Vaccine vaccine = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vaccine, parent, false);
        }

        TextView vaccineName = convertView.findViewById(R.id.item_vaccine_vaccine_name);
        vaccineName.setText(vaccine.getVaccineName());

        //Edit or delete profile:
        View finalConvertView = convertView;
        convertView.setOnLongClickListener(v -> {
                    Intent myIntent = new Intent(finalConvertView.getContext(), VaccineFormActivity.class);
                    myIntent.putExtra("mode", "edit");
                    myIntent.putExtra("content", vaccine);
                    vaccineActivityResultLauncher.launch(myIntent);
                    return true;
                }
        );
        return convertView;
    }
}