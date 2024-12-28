package com.zweckinger.myvaccreg.ui.vaccination;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.zweckinger.myvaccreg.MainActivity;
import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.model.VeccregDatabase;
import com.zweckinger.myvaccreg.model.vaccination.Vaccination;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import com.zweckinger.myvaccreg.model.vaccine.VaccineDao;
import com.zweckinger.myvaccreg.ui.FormActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VaccinationFormActivity extends FormActivity {

    private EditText vaccinationDateEditText;
    private EditText doseEditText;
    private Spinner vaccineSpinner;
    Calendar vaccinationDate;

    public VaccinationFormActivity() {
        super(R.layout.activity_form_vaccination, R.string.action_create_vaccination,
                R.string.action_edit_vaccination);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vaccinationDate = Calendar.getInstance();

        //Vaccination date:
        vaccinationDateEditText = findViewById(R.id.form_vaccination_date_EditText);
        vaccinationDateEditText.setOnClickListener(v -> new DatePickerDialog(vaccinationDateEditText.getContext(),
                (view, year, month, dayOfMonth) -> {
                    vaccinationDate.set(Calendar.YEAR, year);
                    vaccinationDate.set(Calendar.MONTH, month);
                    vaccinationDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setVaccinationDateText(vaccinationDate);
                }, vaccinationDate.get(Calendar.YEAR), vaccinationDate.get(Calendar.MONTH), vaccinationDate.get(Calendar.DAY_OF_MONTH)).show());
        vaccinationDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                checkForm(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        doseEditText = findViewById(R.id.form_vaccination_dose_EditText);
        doseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                checkForm(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        //Load vaccine spinner:
        vaccineSpinner = findViewById(R.id.form_vaccination_vaccine_spinner);
        VeccregDatabase db = VeccregDatabase.instance;
        VaccineDao vaccineDao = db.vaccineDao();

        VeccregDatabase.databaseWriteExecutor.execute(() -> {
            List<Vaccine> vaccines = vaccineDao.getAlphabetizedVaccines();
            VaccineArrayAdapter vaccineArrayAdapter = new VaccineArrayAdapter(this,
                    new ArrayList<>(vaccines));
            vaccineArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vaccineSpinner.post(() -> vaccineSpinner.setAdapter(vaccineArrayAdapter));
        });
    }

    void setVaccinationDateText(Calendar vaccinationDate) {
        //Display date in local date format:
        vaccinationDateEditText.setText(SimpleDateFormat.getDateInstance().format(vaccinationDate.getTime()));
    }

    @Override
    public boolean checkForm(boolean showError) {
        boolean valid = false;
        if (doseEditText.getText().toString().length() > 0 && doseEditText.getText().toString().length() < 4) {
            if (vaccinationDateEditText.getText().toString().length() > 0) {
                if(vaccineSpinner.getSelectedItem() != null) {
                    createEditButton.setEnabled(true);
                    valid = true;
                }
            } else if (showError || wasValidBefore) {
                vaccinationDateEditText.setError(getString(R.string.vaccination_date_error));
            }
        } else if (showError || wasValidBefore) {
            doseEditText.setError(getString(R.string.vaccination_dose_error));
        }

        if (!valid)
            createEditButton.setEnabled(false);
        wasValidBefore = valid;
        return valid;
    }

    /**
     * Used to load data when editing an entry.
     */
    @Override
    public void fillFormEditMode() {
        Vaccination v = (Vaccination) editContent;
        setVaccinationDateText(v.getDate());
        doseEditText.setText("" + v.getDose());
        vaccineSpinner.setSelection(v.getVaccineId());
    }

    /**
     * Used to retrieve data from this activity.
     */
    @Override
    protected Serializable makeContentFromForm() {
        if (mode.equals("create")) {
            return new Vaccination(
                    MainActivity.activeProfile.getProfileId(),
                    ((Vaccine) vaccineSpinner.getSelectedItem()).getVaccineId(),
                    Integer.parseInt(doseEditText.getText().toString()),
                    vaccinationDate);
        } else {
            Vaccination v = (Vaccination) editContent;
            v.setDose(Integer.parseInt(doseEditText.getText().toString()));
            v.setDate(vaccinationDate);
            v.setVaccineId(((Vaccine) vaccineSpinner.getSelectedItem()).getVaccineId());
            return v;
        }
    }
}