package com.zweckinger.myvaccreg.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.databinding.FragmentProfileBinding;
import com.zweckinger.myvaccreg.ui.MasterFragment;

public class ProfileFragment extends MasterFragment {

    private FragmentProfileBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        openFormActivity = ProfileFormActivity.class;
        return binding.getRoot();
    }

    @Override
    public void destroyBinding() {
        binding = null;
    }

    @Override
    protected void setupLiveData() {
        //Load profiles into recycler view:
        RecyclerView recyclerView = root.findViewById(R.id.profiles_recycler_view);

        ProfileViewModel profileViewModel = (ProfileViewModel) viewModel;
        final ProfileListAdapter adapter = new ProfileListAdapter(new ProfileListAdapter.Diff(),
                formActivityResultLauncher, profileViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //Update the cached copy of the profiles in the adapter:
        profileViewModel.getAllProfiles().observe(this, adapter::submitList);
    }
}
