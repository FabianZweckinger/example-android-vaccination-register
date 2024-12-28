package com.zweckinger.myvaccreg.ui.illness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zweckinger.myvaccreg.R;
import com.zweckinger.myvaccreg.databinding.FragmentIllnessBinding;
import com.zweckinger.myvaccreg.ui.MasterFragment;

public class IllnessFragment extends MasterFragment {

    private FragmentIllnessBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIllnessBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(IllnessViewModel.class);
        openFormActivity = IllnessFormActivity.class;
        return binding.getRoot();
    }

    @Override
    public void destroyBinding() {
        binding = null;
    }

    @Override
    protected void setupLiveData() {
        //Load illnesses into recycler view:
        RecyclerView recyclerView = root.findViewById(R.id.illnesses_recycler_view);

        IllnessViewModel illnessViewModel = (IllnessViewModel) viewModel;
        final IllnessListAdapter adapter = new IllnessListAdapter(new IllnessListAdapter.Diff(),
                formActivityResultLauncher, illnessViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Update the cached copy of the illnesses in the adapter:
        illnessViewModel.getAllIllnesses().observe(this, adapter::submitList);
    }
}