package com.example.ndt.sabletid;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;

import java.util.List;

public class UsersSubletsListFragment extends BaseSubletListFragment {
    private static final String ARG_USER_ID = "userId";

    private String userId;

    public static UsersSubletsListFragment newInstance(String userId) {
        UsersSubletsListFragment fragment = new UsersSubletsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }
    @Override
    protected LiveData<List<SubletPost>> GetSublets() {
        SubletPostViewModel subletViewModel = ViewModelProviders.of(this).get(SubletPostViewModel.class);
        return subletViewModel.getSubletPostsByUserId(userId);
    }
}
