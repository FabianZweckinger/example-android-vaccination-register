package com.zweckinger.myvaccreg.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.databinding.FragmentDashboardBinding;
import com.zweckinger.myvaccreg.ui.profile.ProfileFormActivity;
import com.zweckinger.myvaccreg.ui.vaccine.VaccineFormActivity;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView helloTextView = binding.dashboardHello;
        dashboardViewModel.getActiveProfile().observe(getViewLifecycleOwner(), profile -> {
            if(profile != null) {
                helloTextView.setText(getString(R.string.hello) + "\n" + profile.getProfileName());
            }
        });

        //Due vaccination card handler:
        CardView findDoctorCard = root.findViewById(R.id.dashboard_doctor_card_view);
        findDoctorCard.setOnClickListener(v -> {
            //Open google maps and search for doctor
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + getString(R.string.doctor));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        //Due vaccination card handler:
        CardView vaccinationsCertificatesCard = root.findViewById(R.id.dashboard_vaccination_certificates_cardView);
        vaccinationsCertificatesCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Feature not implemented", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}