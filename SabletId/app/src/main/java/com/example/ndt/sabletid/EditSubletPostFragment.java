package com.example.ndt.sabletid;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndt.sabletid.Models.Image.ImageModel;
import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditSubletPostFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_DESCRIPTION = "ARG_DESCRIPTION";
    private static final String ARG_PRICE = "ARG_PRICE";
    private static final String ARG_START_DATE = "ARG_START_DATE";
    private static final String ARG_END_DATE = "ARG_END_DATE";
    private static final String ARG_CITY = "ARG_CITY";
    private static final String ARG_URL = "EditSubletImage.png";

    private static final String ARG_POST_ID = "postId";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private SubletPostViewModel subletPostViewModel;
    private String postId;
    private GoogleMap mMap;
    private ImageView ivImage;
    private SubletPost sublet;

    ProgressBar progressBar;
    private Bitmap imageBitmap;
    EditText etDescription, etPrice, etEndDate, etStartDate, etCity;
    Boolean isImageChanged = false;

    public EditSubletPostFragment() {

    }

    public static EditSubletPostFragment newInstance(String postId) {
        EditSubletPostFragment fragment = new EditSubletPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_sublet_post, container, false);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.edit_sublet_map);
        ivImage = view.findViewById(R.id.ivEditSubletPostEditImage);
        etPrice = view.findViewById(R.id.etEditSubletPostPrice);
        etStartDate = view.findViewById(R.id.etEditSubletPostStartDate);
        etEndDate = view.findViewById(R.id.etEditSubletPostEndDate);
        etCity = view.findViewById(R.id.etEditSubletPostCity);
        etDescription = view.findViewById(R.id.etEditSubletPostDescription);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        final OnMapReadyCallback self = this;

        subletPostViewModel = ViewModelProviders.of(this).get(SubletPostViewModel.class);

        subletPostViewModel.getSubletPostById(postId).observe(this, new Observer<SubletPost>() {
            @Override
            public void onChanged(@Nullable SubletPost subletPost) {
                if(subletPost != null && subletPost.getId() != null) {
                    sublet = subletPost;

                    etPrice.setText(Integer.toString(subletPost.getPrice()));
                    etStartDate.setText(subletPost.getStartDate());
                    etEndDate.setText(subletPost.getEndDate());
                    etCity.setText(subletPost.getCity());
                    etDescription.setText(subletPost.getDescription());

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

        view.findViewById(R.id.btnEditSubletPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SubletPost subletPost = new SubletPost();
                subletPost.setId(postId);
                subletPost.setCity(etCity.getText().toString().trim());
                subletPost.setDescription(etDescription.getText().toString().trim());
                subletPost.setPrice(Integer.parseInt(etPrice.getText().toString().trim()));
                subletPost.setEndDate(etEndDate.getText().toString().trim());
                subletPost.setStartDate(etStartDate.getText().toString().trim());
                subletPost.setLatitude(sublet.getLatitude());
                subletPost.setLongitude(sublet.getLongitude());
                subletPost.setUserId(sublet.getUserId());

                if (imageBitmap != null) {
                    ImageModel.instance.saveImage(imageBitmap, new ImageModel.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            subletPost.setPhoto(url);

                            updateSubletPost(subletPost);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(view.getContext(), "Failed to update post, please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    updateSubletPost(subletPost);
                }
            }
        });

        view.findViewById(R.id.btnEditSubletPostEditImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_IMAGE_CAPTURE);
                }
                else {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        view.findViewById(R.id.btnDeleteSubletPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subletPostViewModel.deleteSubletPost(sublet, new SubletPostViewModel.DeleteSubletPostListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(view.getContext(), "Your sublet post has been deleted successfully",
                                Toast.LENGTH_LONG).show();

                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(view.getContext(), "Failed to delete sublet post, please try again",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

    public void updateSubletPost(SubletPost subletPost) {
        subletPostViewModel.updateSubletPost(subletPost, new SubletPostViewModel.UpdateSubletPostListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getView().getContext(), "Your account has been updated successfully",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getView().getContext(), "Failed to update user, please try again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ivImage.setImageBitmap(imageBitmap);

            isImageChanged = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (sublet != null) {
            LatLng subletLocation = new LatLng(sublet.getLatitude(), sublet.getLongitude());
            mMap.addMarker(new MarkerOptions().position(subletLocation).title("Here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(subletLocation));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        etDescription.setText(savedInstanceState.getString(ARG_DESCRIPTION));
        etStartDate.setText(savedInstanceState.getString(ARG_START_DATE));
        etEndDate.setText(savedInstanceState.getString(ARG_END_DATE));
        etPrice.setText(savedInstanceState.getString(ARG_PRICE));
        etCity.setText(savedInstanceState.getString(ARG_CITY));

        Bitmap bitMap = ImageModel.instance.loadImageFromFile(ARG_URL);
        if (bitMap != null) {
            ivImage.setImageBitmap(bitMap);
            imageBitmap = bitMap;
            ImageModel.instance.DeleteImage(ARG_URL);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putString(ARG_DESCRIPTION, etDescription.getText().toString().trim());
        bundle.putString(ARG_PRICE, etPrice.getText().toString().trim());
        bundle.putString(ARG_START_DATE, etStartDate.getText().toString().trim());
        bundle.putString(ARG_END_DATE, etEndDate.getText().toString().trim());
        bundle.putString(ARG_CITY, etCity.getText().toString().trim());

        ImageModel.instance.saveImageToFile(imageBitmap, ARG_URL);
    }
}
