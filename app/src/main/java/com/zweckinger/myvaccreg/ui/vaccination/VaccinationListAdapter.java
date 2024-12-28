package com.zweckinger.myvaccreg.ui.vaccination;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import com.zweckinger.myvaccreg.model.illness.Illness;
import com.zweckinger.myvaccreg.model.illness.IllnessDao;
import com.zweckinger.myvaccreg.model.profile.Profile;
import com.zweckinger.myvaccreg.model.vaccination.Vaccination;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import com.zweckinger.myvaccreg.model.vaccine.VaccineDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VaccinationListAdapter extends ListAdapter<Vaccination, VaccinationViewHolder> {

    private final ActivityResultLauncher<Intent> vaccinationActivityResultLauncher;

    public VaccinationListAdapter(@NonNull DiffUtil.ItemCallback<Vaccination> diffCallback,
                                  ActivityResultLauncher<Intent> vaccinationActivityResultLauncher) {
        super(diffCallback);
        this.vaccinationActivityResultLauncher = vaccinationActivityResultLauncher;
    }

    @Override
    public VaccinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VaccinationViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(VaccinationViewHolder holder, int position) {
        Vaccination current = getItem(position);

        //Edit or delete vaccination:
        holder.itemView.setOnLongClickListener(v -> {
                    Intent myIntent = new Intent(holder.itemView.getContext(), VaccinationFormActivity.class);
                    myIntent.putExtra("mode", "edit");
                    myIntent.putExtra("content", current);
                    vaccinationActivityResultLauncher.launch(myIntent);
                    return true;
                }
        );

        VeccregDatabase db = VeccregDatabase.instance;
        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            VaccineDao vaccineDao = db.vaccineDao();

            //Get vaccine name:
            Vaccine vaccine = vaccineDao.getVaccine(current.getVaccineId());

            //Get illnesses:
            Illness i = vaccineDao.getIllness(current.getVaccineId());

            if (i != null) {
                String illnessName = i.getIllnessName();

                String dateString = SimpleDateFormat.getDateInstance().format(current.getDate().getTime());

                holder.itemView.post(() -> {
                    int maxDose = vaccine.getBasicImmunizations().size();
                    boolean nextIsBooster = current.getDose() > vaccine.getBasicImmunizations().size() - 1;
                    boolean nextVaccRequired = nextIsBooster && vaccine.getBoosterVaccinationRepeating() != -1 || current.getDose() < vaccine.getBasicImmunizations().size() + 1;

                    String nextVaccType = "";
                    String nextDateString = "";

                    if (nextVaccRequired) {
                        //Next vaccination type (basic or booster):
                        if (nextIsBooster) {
                            nextVaccType = holder.itemView.getContext().getString(R.string.next_booster_immunisation);
                        } else {
                            nextVaccType = holder.itemView.getContext().getString(R.string.next_basic_immunisation);
                        }

                        //Next vaccination date:
                        Calendar nextDate = (Calendar) current.getDate().clone(); //Clone object to remove previous reference
                        if (nextIsBooster) {
                            nextDate.add(Calendar.MONTH, vaccine.getBoosterVaccinationRepeating());
                        } else {
                            nextDate.add(Calendar.MONTH, vaccine.getBasicImmunizations().get(current.getDose()));
                        }
                        nextDateString = SimpleDateFormat.getDateInstance().format(nextDate.getTime());
                    }

                    String doseText = "";
                    if (nextIsBooster) {
                        doseText = holder.itemView.getContext().getString(R.string.dose) + current.getDose();
                    } else {
                        doseText = holder.itemView.getContext().getString(R.string.dose) + current.getDose() + "/" + maxDose;
                    }

                    holder.bind(vaccine.getVaccineName(), doseText, dateString, illnessName, nextVaccType, nextDateString);
                });
            }
        });
    }

    public static class Diff extends DiffUtil.ItemCallback<Vaccination> {

        @Override
        public boolean areItemsTheSame(@NonNull Vaccination oldItem, @NonNull Vaccination newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Vaccination oldItem, @NonNull Vaccination newItem) {
            return oldItem.getVaccinationId() == newItem.getVaccinationId();
        }
    }
}