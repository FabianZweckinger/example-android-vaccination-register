package com.zweckinger.myvaccreg.ui.profile;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.zweckinger.myvaccreg.MainActivity;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import com.zweckinger.myvaccreg.model.profile.Profile;

public class ProfileListAdapter extends ListAdapter<Profile, ProfileViewHolder> {

    private ProfileViewModel profileViewModel;
    private final ActivityResultLauncher<Intent> profileActivityResultLauncher;

    public ProfileListAdapter(@NonNull DiffUtil.ItemCallback<Profile> diffCallback,
                              ActivityResultLauncher<Intent> profileActivityResultLauncher,
                              ProfileViewModel profileViewModel) {
        super(diffCallback);
        this.profileActivityResultLauncher = profileActivityResultLauncher;
        this.profileViewModel = profileViewModel;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ProfileViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        Profile current = getItem(position);

        //Edit or delete profile:
        holder.itemView.setOnLongClickListener(v -> {
                    Intent myIntent = new Intent(holder.itemView.getContext(), ProfileFormActivity.class);
                    myIntent.putExtra("mode", "edit");
                    myIntent.putExtra("content", current);
                    myIntent.putExtra("noDelete", current.isActive());
                    profileActivityResultLauncher.launch(myIntent);
                    return true;
                }
        );

        //Select profile:
        holder.itemView.setOnClickListener(v -> {
            VeccregDatabase.databaseWriteExecutor.execute(() -> {

                //Disable all active profiles:
                for (int i = 0; i < getItemCount(); i++) {
                    Profile p = getItem(i);
                    if(p.isActive()) {
                        p.setActive(false);
                        profileViewModel.update(p);
                    }
                }

                //Enable only select profile:
                MainActivity.activeProfile = current;
                current.setActive(true);
                profileViewModel.update(current);
            });
        });

        holder.bind(current.getProfileName(), current.isActive());
    }

    public static class Diff extends DiffUtil.ItemCallback<Profile> {

        @Override
        public boolean areItemsTheSame(@NonNull Profile oldItem, @NonNull Profile newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Profile oldItem, @NonNull Profile newItem) {
            return oldItem.getProfileName().equals(newItem.getProfileName());
        }
    }
}