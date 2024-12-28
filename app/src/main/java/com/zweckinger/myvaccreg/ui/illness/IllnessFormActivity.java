package com.zweckinger.myvaccreg.ui.illness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.Utils;
import com.zweckinger.myvaccreg.model.illness.Illness;
import com.zweckinger.myvaccreg.model.vaccination.Vaccination;
import com.zweckinger.myvaccreg.model.vaccine.Vaccine;
import com.zweckinger.myvaccreg.ui.FormActivity;
import com.zweckinger.myvaccreg.ui.vaccine.VaccineFormActivity;
import com.zweckinger.myvaccreg.ui.vaccine.VaccineListAdapter;
import com.zweckinger.myvaccreg.ui.vaccine.VaccineViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IllnessFormActivity extends FormActivity {

    private ActivityResultLauncher<Intent> vaccineActivityResultLauncher;
    private EditText illnessNameEditText;
    private VaccineViewModel vaccineViewModel;

    public IllnessFormActivity() {
        super(R.layout.activity_form_illness, R.string.action_create_illness,
                R.string.action_edit_illness);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Illness illness = (Illness) editContent;

        vaccineViewModel = new ViewModelProvider(this).get(VaccineViewModel.class);

        illnessNameEditText = findViewById(R.id.form_illness_name_edit_text);
        illnessNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                checkForm(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        //Vaccine activity result:
        vaccineActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String mode = data.getStringExtra("mode");
                            Serializable content = data.getSerializableExtra("content");
                            if (content != null) {
                                System.out.println(mode);
                                switch (mode) {
                                    case "create":
                                        vaccineViewModel.insert(content);
                                        reloadVaccines(illness);
                                        break;

                                    case "edit":
                                        vaccineViewModel.update(content);
                                        reloadVaccines(illness);
                                        break;

                                    case "delete":
                                        vaccineViewModel.delete(content);
                                        reloadVaccines(illness);
                                        break;

                                    default:
                                        Utils.showError("Database unknown mode error");
                                }
                            } else {
                                Utils.showError("Database name error");
                            }
                        } else {
                            Utils.showError("Database result error");
                        }
                    }
                });

        //Add vaccine button:
        Button addVaccineButton = findViewById(R.id.form_illness_add_vaccine_button);
        if(mode.equals("create")) {
            //Remove vaccines view and add vaccine button:
            RelativeLayout vaccinesRelativeLayout = findViewById(R.id.form_illness_vaccines_relativeLayout);
            ((ViewManager)vaccinesRelativeLayout.getParent()).removeView(vaccinesRelativeLayout);
            ((ViewManager)addVaccineButton.getParent()).removeView(addVaccineButton);
        }else {
            reloadVaccines(illness);
        }

        addVaccineButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(v.getContext(), VaccineFormActivity.class);
            myIntent.putExtra("mode", "create");
            myIntent.putExtra("illnessId", illness.getIllnessId());
            vaccineActivityResultLauncher.launch(myIntent);
        });
    }

    private void reloadVaccines(Illness illness) {
        //Load vaccines into list view:
        ListView listView = findViewById(R.id.form_illness_vaccines_listview);

        // Create the observer which updates the UI.
        final Observer<List<Vaccine>> nameObserver = new Observer<List<Vaccine>>() {
            @Override
            public void onChanged(@Nullable final List<Vaccine> vaccines) {
                List<Vaccine> vaccinesForIllness = new ArrayList<>();
                for (Vaccine vaccine : vaccines) {
                    if(vaccine.illnessId == illness.getIllnessId()) {
                        vaccinesForIllness.add(vaccine);
                    }
                }
                final VaccineListAdapter adapter = new VaccineListAdapter(listView.getContext(),
                        vaccinesForIllness, vaccineActivityResultLauncher);
                listView.setAdapter(adapter);

                TextView noElementsText = findViewById(R.id.form_illness_no_vaccines_textView);
                if (vaccinesForIllness.size() > 0) {
                    noElementsText.setVisibility(View.INVISIBLE);
                } else {
                    noElementsText.setVisibility(View.VISIBLE);
                }
            }
        };

        vaccineViewModel.getAllVaccines().observe(this, nameObserver);
    }

    @Override
    public boolean checkForm(boolean showError) {
        boolean valid = false;
        if (illnessNameEditText.getText().toString().length() > 2) {
            if (illnessNameEditText.getText().toString().length() < 20) {
                createEditButton.setEnabled(true);
                valid = true;
            } else if (showError || wasValidBefore) {
                illnessNameEditText.setError(getString(R.string.illness_name_too_long));
            }
        } else if (showError || wasValidBefore) {
            illnessNameEditText.setError(getString(R.string.illness_name_too_short));
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
        Illness i = (Illness) editContent;
        illnessNameEditText.setText(i.getIllnessName());
    }

    /**
     * Used to retrieve data from this activity.
     */
    @Override
    protected Serializable makeContentFromForm() {
        if (mode.equals("create")) {
            return new Illness(illnessNameEditText.getText().toString());
        } else {
            Illness i = (Illness) editContent;
            i.setIllnessName(illnessNameEditText.getText().toString());
            return i;
        }
    }
}