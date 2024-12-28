package com.zweckinger.myvaccreg.ui.illness;

import android.content.Intent;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import com.zweckinger.myvaccreg.model.illness.Illness;
import com.zweckinger.myvaccreg.model.vaccination.Vaccination;

public class IllnessListAdapter extends ListAdapter<Illness, IllnessViewHolder> {

    private IllnessViewModel illnessViewModel;
    private final ActivityResultLauncher<Intent> illnessActivityResultLauncher;

    public IllnessListAdapter(@NonNull DiffUtil.ItemCallback<Illness> diffCallback,
                              ActivityResultLauncher<Intent> illnessActivityResultLauncher,
                              IllnessViewModel illnessViewModel) {
        super(diffCallback);
        this.illnessActivityResultLauncher = illnessActivityResultLauncher;
        this.illnessViewModel = illnessViewModel;
    }

    @Override
    public IllnessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return IllnessViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(IllnessViewHolder holder, int position) {
        Illness current = getItem(position);

        //Edit or delete profile:
        holder.itemView.setOnLongClickListener(v -> {
                    Intent myIntent = new Intent(holder.itemView.getContext(), IllnessFormActivity.class);
                    myIntent.putExtra("mode", "edit");
                    myIntent.putExtra("content", current);
                    illnessActivityResultLauncher.launch(myIntent);
                    return true;
                }
        );

        //Select profile:
        holder.itemView.setOnClickListener(v -> {
            VeccregDatabase.databaseWriteExecutor.execute(() -> {
            });
        });

        holder.bind(current.getIllnessName());
    }

    public static class Diff extends DiffUtil.ItemCallback<Illness> {

        @Override
        public boolean areItemsTheSame(@NonNull Illness oldItem, @NonNull Illness newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Illness oldItem, @NonNull Illness newItem) {
            return oldItem.getIllnessName().equals(newItem.getIllnessName());
        }
    }
}