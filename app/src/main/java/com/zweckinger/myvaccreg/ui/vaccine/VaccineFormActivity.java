package com.zweckinger.myvaccreg.ui.vaccine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.Utils;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import com.zweckinger.myvaccreg.ui.FormActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class VaccineFormActivity extends FormActivity {

    private EditText vaccineNameEditText;
    private LinearLayout boosterLayout;
    private LinearLayout basicImmuLayout;
    private int vaccineId = -1;
    private Spinner[] basicImmuSpinners;
    private EditText[] basicImmuEditTexts;
    private static final int MAX_EXTRA_BASIC_IMMUNISATIONS = 5;
    private Spinner boosterSpinner;
    private EditText boosterEditText;

    public VaccineFormActivity() {
        super(R.layout.activity_form_vaccine, R.string.action_create_vaccine,
                R.string.action_edit_vaccine);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vaccineNameEditText = findViewById(R.id.form_vaccine_name_edit_text);
        vaccineNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                checkForm(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        Intent intent = getIntent();
        vaccineId = intent.getIntExtra("illnessId", -1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dates_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //TODO Update: Only create 1 basic immunizations, and create new when last was filled:
        basicImmuLayout = findViewById(R.id.form_vaccine_basicimmu_linearLayout);
        basicImmuSpinners = new Spinner[MAX_EXTRA_BASIC_IMMUNISATIONS];
        basicImmuEditTexts = new EditText[MAX_EXTRA_BASIC_IMMUNISATIONS];
        for (int i = 0; i < MAX_EXTRA_BASIC_IMMUNISATIONS; i++) {
            View c = getLayoutInflater().inflate(R.layout.item_basicimmu, null);
            Spinner s = c.findViewById(R.id.item_basicImmu_datetype);
            TextView t = c.findViewById(R.id.item_basicImmu_textView);
            t.setText(i+2 + getString(R.string.x_basic_immunisation));
            s.setAdapter(adapter);
            s.setSelection(0);
            basicImmuSpinners[i] = s;
            basicImmuEditTexts[i] = c.findViewById(R.id.item_basicImmu_title_editTextNumber);
            basicImmuEditTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable arg0) {
                    checkForm(true);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
            basicImmuLayout.addView(c);
        }

        boosterLayout = findViewById(R.id.form_vaccine_booster_linearLayout);
        View child = getLayoutInflater().inflate(R.layout.item_booster, null);
        Spinner s2 = child.findViewById(R.id.item_boosterImmu_datetype);
        s2.setAdapter(adapter);
        s2.setSelection(1);
        boosterSpinner = s2;
        boosterEditText = child.findViewById(R.id.item_booster_title_editTextNumber);
        boosterLayout.addView(child);

        basicImmuLayout = findViewById(R.id.form_vaccine_basicimmu_linearLayout);
    }

    @Override
    public boolean checkForm(boolean showError) {
        boolean valid = false;
        if (vaccineNameEditText.getText().toString().length() > 2) {
            if (vaccineNameEditText.getText().toString().length() < 20) {
                createEditButton.setEnabled(true);
                if(validateBasicImmus()) {
                    valid = true;
                }else {
                    Utils.showError("Invalid basic immunisation date");
                }

            } else if (showError || wasValidBefore) {
                vaccineNameEditText.setError(getString(R.string.vaccine_name_too_long));
            }
        } else if (showError || wasValidBefore) {
            vaccineNameEditText.setError(getString(R.string.vaccine_name_too_short));
        }

        if (!valid)
            createEditButton.setEnabled(false);
        wasValidBefore = valid;
        return valid;
    }

    private boolean validateBasicImmus() {
        boolean foundElement = false;
        for (int i = basicImmuEditTexts.length - 1; i >= 0; i--) {
            EditText editText = basicImmuEditTexts[i];
            if(!foundElement) {
                if (!editText.getText().toString().equals("")) {
                    foundElement = true;
                }
            }else{
                if (editText.getText().toString().equals("")) {
                   return false;
                }
            }
        }
        return true;
    }

    /**
     * Used to load data when editing an entry.
     */
    @Override
    public void fillFormEditMode() {
        Vaccine v = (Vaccine) editContent;
        vaccineNameEditText.setText(v.getVaccineName());

        for (int i = 0; i < v.getBasicImmunizations().size(); i++) {
            Integer months = v.getBasicImmunizations().get(i);
            if (months != null) {
                if (inYears(months)) {
                    basicImmuSpinners[i].setSelection(1);
                    basicImmuEditTexts[i].setText("" + Math.round(months / 12));
                } else {
                    basicImmuSpinners[i].setSelection(0);
                    basicImmuEditTexts[i].setText("" + months);
                }
            }
        }

        int booster = v.getBoosterVaccinationRepeating();
        if(booster > 0) {
            if (inYears(booster)) {
                boosterSpinner.setSelection(1);
                boosterEditText.setText("" + Math.round(booster / 12));
            } else {
                boosterSpinner.setSelection(0);
                boosterEditText.setText("" + booster);
            }
        }
    }

    private Boolean inYears(int months) {
        if(months > 11) {
            return months % 12 == 0;
        }
        return false;
    }

    /**
     * Used to retrieve data from this activity.
     */
    @Override
    protected Serializable makeContentFromForm() {
        //Basic Immunisation:
        ArrayList<Integer> basicImmus = new ArrayList<>();
        boolean lastValid = true;
        for (int i = 0; i < basicImmuEditTexts.length; i++) {
            if (basicImmuEditTexts[i].getText().toString().length() > 0) {
                if(lastValid) {
                    boolean isYear = basicImmuSpinners[i].getSelectedItem().toString().equals("Years");
                    int months = Integer.parseInt(basicImmuEditTexts[i].getText().toString());
                    if (isYear) {
                        months *= 12;
                    }
                    basicImmus.add(months);
                    lastValid = true;
                }
            }else{
                lastValid = false;
            }
        }

        //Booster:
        int boosterMonths = -1;
        if(boosterEditText.getText().toString().length() > 0) {
            boosterMonths = Integer.parseInt(boosterEditText.getText().toString());
            if(boosterMonths > 0) {
                if (boosterSpinner.getSelectedItem().toString().equals("Years")) {
                    boosterMonths *= 12;
                }
            }
        }

        if (mode.equals("create")) {
            return new Vaccine(vaccineNameEditText.getText().toString(), vaccineId, basicImmus, boosterMonths);
        } else {
            Vaccine i = (Vaccine) editContent;
            i.setVaccineName(vaccineNameEditText.getText().toString());
            i.setBasicImmunizations(basicImmus);
            i.setBoosterVaccinationRepeating(boosterMonths);
            return i;
        }
    }
}