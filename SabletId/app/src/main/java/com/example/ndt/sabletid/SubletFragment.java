package com.example.ndt.sabletid;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubletFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_SUBLET_ID = "subletId";

    private GoogleMap mMap;
    private SubletPost sublet;
    private String subletId;

    public SubletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param subletId The sublet id.
     * @return A new instance of fragment SubletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubletFragment newInstance(String subletId) {
        SubletFragment fragment = new SubletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBLET_ID, subletId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subletId = getArguments().getString(ARG_SUBLET_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sublet, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.sublet_map);
        mapFragment.getMapAsync(this);

        SubletPostViewModel subletViewModel = ViewModelProviders.of(this).get(SubletPostViewModel.class);
        
        sublet = subletViewModel.getSubletPostById(subletId).getValue();

        if(sublet != null) {
            ((TextView) view.findViewById(R.id.sfv_price)).setText(sublet.getPrice());
            ((TextView) view.findViewById(R.id.sfv_from)).setText(sublet.getStartDate());
            ((TextView) view.findViewById(R.id.sfv_to)).setText(sublet.getEndDate());
            ((TextView) view.findViewById(R.id.sfv_description)).setText(sublet.getDescription());
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(sublet != null) {
            LatLng subletLocation = new LatLng(sublet.getLatitude(), sublet.getLongitude());
            mMap.addMarker(new MarkerOptions().position(subletLocation).title("Here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(subletLocation));
        }
    }
}
