package com.francescosalamone.backingapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.francescosalamone.backingapp.DetailActivity;
import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.databinding.FragmentStepDetailBinding;


public class StepDetailFragment extends Fragment {

    FragmentStepDetailBinding mBinding;

    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        View rootView = mBinding.getRoot();

        Intent intent = getActivity().getIntent();
        int position = intent.getIntExtra(DetailActivity.ITEM_POSITION, 0);
        Toast.makeText(getActivity(), "item clicked: " + position, Toast.LENGTH_SHORT).show();

        return rootView;
    }

}
