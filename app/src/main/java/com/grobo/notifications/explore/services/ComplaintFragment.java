package com.grobo.notifications.explore.services;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.grobo.notifications.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComplaintFragment extends Fragment {


    public ComplaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complaint, container, false);
    }

}
