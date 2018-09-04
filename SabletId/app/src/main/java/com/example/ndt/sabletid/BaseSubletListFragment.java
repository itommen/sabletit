package com.example.ndt.sabletid;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSubletListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private ArrayList<SubletPost> Sublets;
    private SubletPostArrayAdapter ArrayAdapter;

    public BaseSubletListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Sublets = new ArrayList<>();
        ArrayAdapter = new SubletPostArrayAdapter(getContext(), Sublets);
        ArrayAdapter.setNotifyOnChange(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_sublets_list, container, false);

        view.findViewById(R.id.fab_new_sublet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.content_frame, new CreateNewSubletPostFragment());
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }

    protected abstract LiveData<List<SubletPost>> GetSublets();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(ArrayAdapter);

        GetSublets().observe(this, new Observer<List<SubletPost>>() {
            @Override
            public void onChanged(@Nullable List<SubletPost> subletPosts) {
                ArrayAdapter.clear();
                ArrayAdapter.addAll(subletPosts);
            }
        });

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        Fragment newFr = SingleSubletPostFragment.newInstance(Sublets.get(position).getId());
        transaction.replace(R.id.content_frame, newFr);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
