package com.zweckinger.myvaccreg.ui.vaccine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zweckinger.myvaccreg.R;

class VaccineViewHolder extends RecyclerView.ViewHolder {

    private final TextView vaccineItemView;

    private VaccineViewHolder(View itemView) {
        super(itemView);
        vaccineItemView = itemView.findViewById(R.id.item_vaccine_vaccine_name);
    }

    public void bind(String text) {
        vaccineItemView.setText(text);
    }

    static VaccineViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vaccine, parent, false);
        return new VaccineViewHolder(view);
    }
}