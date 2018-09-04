package com.example.ndt.sabletid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private HashMap<Marker, String> MarkerToPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.activity_maps2, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        SubletPostViewModel viewModel = ViewModelProviders.of(this).get(SubletPostViewModel.class);

        googleMap.setOnMarkerClickListener(this);

        viewModel.getAllSubletPosts().observe(this, new Observer<List<SubletPost>>() {
            @Override
            public void onChanged(@Nullable List<SubletPost> subletPosts) {
                mMap.clear();

                if(subletPosts.size() > 0) {
                    MarkerToPost = new HashMap<>();
                    for (SubletPost sublet : subletPosts) {
                        LatLng location = new LatLng(sublet.getLatitude(), sublet.getLongitude());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(String.format("%s - %s", sublet.getStartDate(), sublet.getEndDate())));

                        MarkerToPost.put(marker, sublet.getId());
                    }

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(subletPosts.get(0).getLatitude(), subletPosts.get(0).getLongitude())));
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String subletId = MarkerToPost.get(marker);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.content_frame, SingleSubletPostFragment.newInstance(subletId));
        transaction.addToBackStack(null);

        transaction.commit();

        return true;
    }
}
