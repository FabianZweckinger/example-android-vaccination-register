package com.zweckinger.myvaccreg.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zweckinger.myvaccreg.ImportExportActivity;
import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.SettingsActivity;
import com.zweckinger.myvaccreg.Utils;
import com.zweckinger.myvaccreg.ui.profile.ProfileFormActivity;

import java.io.Serializable;

public abstract class MasterFragment extends Fragment {

    protected ActivityResultLauncher<Intent> formActivityResultLauncher;
    protected View root;
    protected Class openFormActivity;
    protected MasterViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState)
    {
        root = provideFragmentView(inflater, parent, savedInstanseState);

        formActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String mode = data.getStringExtra("mode");
                            Serializable content = data.getSerializableExtra("content");
                            if (content != null) {
                                switch (mode) {
                                    case "create":
                                        viewModel.insert(content);
                                        break;

                                    case "edit":
                                        viewModel.update(content);
                                        break;

                                    case "delete":
                                        viewModel.delete(content);
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

        FloatingActionButton createProfileButton = root.findViewById(R.id.open_form_floating_button);
        createProfileButton.setOnClickListener(v -> {
            startOpenFormActivity();
        });

        setupLiveData();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyBinding();
    }

    private void startOpenFormActivity() {
        Intent myIntent = new Intent(getContext(), openFormActivity);
        myIntent.putExtra("mode", "create");
        formActivityResultLauncher.launch(myIntent);
    }

    public abstract View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    public abstract void destroyBinding();

    protected abstract void setupLiveData();
}
