package com.example.ndt.sabletid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AllSubletsFragment extends Fragment {
    public AllSubletsFragment() {
        // Required empty public constructor
    }

    public static AllSubletsFragment newInstance() {
        AllSubletsFragment fragment = new AllSubletsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_sublets, container, false);
    }
}
