package com.example.ndt.sabletid;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ndt.sabletid.Models.Image.ImageModel;
import com.example.ndt.sabletid.Models.SubletPost.SubletPost;
import com.example.ndt.sabletid.ViewModels.SubletPostViewModel;
import com.example.ndt.sabletid.ViewModels.UserViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

public class CreateNewSubletPostFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseUser connectedUser;
    private SubletPostViewModel subletPostViewModel;
    private UserViewModel userViewModel;

    private Bitmap imageBitmap;
    private double latitude;
    private double longtitude;
    private int price;
    private String description, startDate, endDate, city, imageUrl;

    ProgressBar progressBar;
    ImageView ivImage;
    EditText etDescription, etPrice, etEndDate, etStartDate, etCity;
    SupportPlaceAutocompleteFragment autocompleteFragment;

    Boolean isImageChanged = false;
    Boolean isInstanceState = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_create_new_sublet_post, container, false);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        subletPostViewModel = ViewModelProviders.of(this).get(SubletPostViewModel.class);

        progressBar = view.findViewById(R.id.createSubletPostIndeterminateBar);
        connectedUser = userViewModel.getFirebaseUser();
        etDescription = view.findViewById(R.id.etCreateSubletPostDescription);
        etPrice = view.findViewById(R.id.etCreateSubletPostPrice);
        etStartDate = view.findViewById(R.id.etCreateSubletPostStartDate);
        etEndDate = view.findViewById(R.id.etCreateSubletPostEndDate);
        etCity = view.findViewById(R.id.etCreateSubletPostCity);
        autocompleteFragment = (SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        ivImage = view.findViewById(R.id.ivCreateSubletPostEditImage);

        progressBar.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        view.findViewById(R.id.btnSaveNewPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                description = etDescription.getText().toString().trim();
                price = Integer.parseInt(etPrice.getText().toString().trim());
                startDate = etStartDate.getText().toString().trim();
                endDate = etEndDate.getText().toString().trim();
                city = etCity.getText().toString().trim();

                if (imageBitmap != null) {
                    ImageModel.instance.saveImage(imageBitmap, new ImageModel.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            imageUrl = url;

                            saveNewSubletPost();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(view.getContext(), "Failed to save a new post, please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    saveNewSubletPost();
                }
            }
        });

        view.findViewById(R.id.btnCreateSubletPostEditImage).setOnClickListener(new View.OnClickListener() {
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
            }});

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longtitude = latLng.longitude;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(view.getContext(), "Failed to change location, please try again",
                        Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                ivImage.setImageBitmap(imageBitmap);

                isImageChanged = true;
        }
    }

    public void saveNewSubletPost() {
        final SubletPost subletPost = new SubletPost(connectedUser.getUid(), description,
                startDate, endDate, city, imageUrl, price, latitude, longtitude);
        subletPostViewModel.addNewSubletPost(subletPost, new SubletPostViewModel.AddNewSubletPostListener() {
            @Override
            public void onSuccess(SubletPost subletPost) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getView().getContext(), "Your post has been published successfully",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getView().getContext(), "Failed to save your post, please try again",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    Toast.makeText(getView().getContext(), "Can't open camera without permissions",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

//        etUserName.setText(savedInstanceState.getString(ARG_NAME));
//        etEmail.setText(savedInstanceState.getString(ARG_EMAIL));
//        etPhone.setText(savedInstanceState.getString(ARG_PHONE));
//        rbFemale.setChecked(savedInstanceState.getBoolean(ARG_GENDER));
//        rbMale.setChecked(!savedInstanceState.getBoolean(ARG_GENDER));
//
////        Bitmap bitMap = ImageModel.instance.loadImageFromFile(ARG_URL);
////        if (bitMap != null) {
////            ivImage.setImageBitmap(bitMap);
////            imageBitmap = bitMap;
////            ImageModel.instance.DeleteImage(ARG_URL);
////        }
//
        isInstanceState = true;
    }
}
