package com.zweckinger.myvaccreg.ui.illness;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zweckinger.myvaccreg.R;

class IllnessViewHolder extends RecyclerView.ViewHolder {

    private final TextView illnessItemView;

    private IllnessViewHolder(View itemView) {
        super(itemView);
        illnessItemView = itemView.findViewById(R.id.item_illness_illness_name);
    }

    public void bind(String text) {
        illnessItemView.setText(text);
    }

    static IllnessViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_illness, parent, false);
        return new IllnessViewHolder(view);
    }
}