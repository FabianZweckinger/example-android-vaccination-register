package com.zweckinger.myvaccreg.ui.vaccination;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.databinding.FragmentVaccinationsBinding;
import com.zweckinger.myvaccreg.ui.MasterFragment;

public class VaccinationFragment extends MasterFragment {

    private FragmentVaccinationsBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVaccinationsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(VaccinationViewModel.class);
        openFormActivity = VaccinationFormActivity.class;
        return binding.getRoot();
    }

    @Override
    public void destroyBinding() {
        binding = null;
    }

    @Override
    protected void setupLiveData() {
        //Load vaccinations into recycler view:
        RecyclerView recyclerView = root.findViewById(R.id.vaccination_recyclerView);

        VaccinationViewModel vaccinationViewModel = (VaccinationViewModel) viewModel;
        final VaccinationListAdapter adapter = new VaccinationListAdapter(new VaccinationListAdapter.Diff(),
                formActivityResultLauncher);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Setup a shared preference listener when vaccine display type changes:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs1, key) -> {
            if (key.equals("vaccination_all_or_latest")) {
                setupListObserver(vaccinationViewModel, adapter);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);

        setupListObserver(vaccinationViewModel, adapter);
    }

    private void setupListObserver(VaccinationViewModel vaccinationViewModel, VaccinationListAdapter adapter) {
        if (!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("vaccination_all_or_latest", false)) {
            //Update the cached copy of the vaccinations in the adapter:
            vaccinationViewModel.getAllVaccinations().observe(this, adapter::submitList);
        } else {
            //Update the cached copy of the vaccinations in the adapter:
            vaccinationViewModel.getVaccinationsOnlyLatestPerVaccine().observe(this, adapter::submitList);
        }
    }
}
