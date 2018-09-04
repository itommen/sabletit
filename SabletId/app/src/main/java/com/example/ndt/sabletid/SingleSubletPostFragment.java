package com.example.ndt.sabletid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndt.sabletid.Models.Image.ImageModel;
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
 * Use the {@link SingleSubletPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleSubletPostFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_SUBLET_ID = "subletId";

    private GoogleMap mMap;
    private ImageView ivImage;
    private SubletPost sublet;
    private String subletId;

    public SingleSubletPostFragment() {
        // Required empty public constructor
    }

    public static SingleSubletPostFragment newInstance(String subletId) {
        SingleSubletPostFragment fragment = new SingleSubletPostFragment();
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
        final View view = inflater.inflate(R.layout.fragment_sublet, container, false);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.sublet_map);
        ivImage = view.findViewById(R.id.ivSubletPostImage);

        final OnMapReadyCallback self = this;

        SubletPostViewModel subletViewModel = ViewModelProviders.of(this).get(SubletPostViewModel.class);
        subletViewModel.getSubletPostById(subletId).observe(this, new Observer<SubletPost>() {
            @Override
            public void onChanged(@Nullable SubletPost subletPost) {
                if(subletPost.getId() != null) {
                    sublet = subletPost;

                    ((TextView) view.findViewById(R.id.sfv_price)).setText(Integer.toString(subletPost.getPrice()));
                    ((TextView) view.findViewById(R.id.sfv_from)).setText(subletPost.getStartDate());
                    ((TextView) view.findViewById(R.id.sfv_to)).setText(subletPost.getEndDate());
                    ((TextView) view.findViewById(R.id.sfv_city)).setText(subletPost.getCity());
                    ((TextView) view.findViewById(R.id.sfv_description)).setText(subletPost.getDescription());

                    String photoUrl = subletPost.getPhoto();

                    if (photoUrl != null) {
                        ImageModel.instance.getImage(photoUrl, new ImageModel.GetImageListener() {
                            @Override
                            public void onDone(Bitmap bitmap) {
                                if (bitmap != null) {
                                    ivImage.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                    else {
                        ivImage.setImageResource(R.drawable.ic_launcher);
                    }

                    mapFragment.getMapAsync(self);
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(sublet != null) {
            LatLng subletLocation = new LatLng(sublet.getLatitude(), sublet.getLongitude());
            mMap.addMarker(new MarkerOptions().position(subletLocation).title("Here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(subletLocation));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }
}
