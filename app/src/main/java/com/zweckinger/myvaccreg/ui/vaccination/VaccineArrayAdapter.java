package com.zweckinger.myvaccreg.ui.vaccination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import java.util.ArrayList;
import java.util.List;

public class VaccineArrayAdapter extends ArrayAdapter<Vaccine> {

    private Context context;
    private List<Vaccine> vaccineList;

    public VaccineArrayAdapter(@NonNull Context context, ArrayList<Vaccine> list) {
        super(context, 0 , list);
        this.context = context;
        vaccineList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent,false);

        Vaccine current = vaccineList.get(position);

        TextView name = (TextView) listItem.findViewById(android.R.id.text1);
        name.setText(current.getVaccineName());

        return listItem;
    }
}