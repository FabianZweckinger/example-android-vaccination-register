package com.zweckinger.myvaccreg.ui.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.model.profile.Profile;
import com.zweckinger.myvaccreg.ui.FormActivity;
import java.io.Serializable;

public class ProfileFormActivity extends FormActivity {

    private EditText profileNameEditText;

    public ProfileFormActivity() {
        super(R.layout.activity_form_profile, R.string.action_create_profile,
                R.string.action_edit_profile);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileNameEditText = findViewById(R.id.form_profile_name_edit_text);
        profileNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                checkForm(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    @Override
    public boolean checkForm(boolean showError) {
        boolean valid = false;
        if (profileNameEditText.getText().toString().length() > 0) {
            if (profileNameEditText.getText().toString().length() < 20) {
                createEditButton.setEnabled(true);
                valid = true;
            } else if (showError || wasValidBefore) {
                profileNameEditText.setError(getString(R.string.profile_name_too_long));
            }
        } else if (showError || wasValidBefore) {
            profileNameEditText.setError(getString(R.string.profile_name_too_short));
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
        Profile p = (Profile) editContent;
        profileNameEditText.setText(p.getProfileName());
    }

    /**
     * Used to retrieve data from this activity.
     */
    @Override
    protected Serializable makeContentFromForm() {
        if (mode.equals("create")) {
            return new Profile(profileNameEditText.getText().toString());
        } else {
            Profile p = (Profile) editContent;
            p.setProfileName(profileNameEditText.getText().toString());
            return p;
        }
    }
}