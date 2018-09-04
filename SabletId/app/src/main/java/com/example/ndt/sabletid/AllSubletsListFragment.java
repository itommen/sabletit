package com.example.ndt.sabletid;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AllSubletsListFragment extends BaseSubletListFragment {

    @Override
    protected LiveData<List<SubletPost>> GetSublets() {
        SubletPostViewModel subletViewModel = ViewModelProviders.of(this).get(SubletPostViewModel.class);
        return subletViewModel.getAllSubletPosts();
    }
}
