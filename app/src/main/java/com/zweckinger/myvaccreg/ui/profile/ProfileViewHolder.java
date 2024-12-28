package com.zweckinger.myvaccreg.ui.profile;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zweckinger.myvaccreg.R;

class ProfileViewHolder extends RecyclerView.ViewHolder {

    private final TextView profileItemView;
    private final ImageView profileIsActiveImageView;

    private ProfileViewHolder(View itemView) {
        super(itemView);
        profileItemView = itemView.findViewById(R.id.item_profile_name_person_name);
        profileIsActiveImageView = itemView.findViewById(R.id.item_profile_is_active);
    }

    public void bind(String text, boolean isActive) {
        profileItemView.setText(text);
        profileItemView.setTypeface(null, Typeface.BOLD);
        profileIsActiveImageView.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
    }

    static ProfileViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }
}