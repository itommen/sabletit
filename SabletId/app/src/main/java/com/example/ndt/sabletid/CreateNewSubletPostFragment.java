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
import android.support.v4.app.FragmentTransaction;
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

import static android.text.style.TtsSpan.ARG_GENDER;

public class CreateNewSubletPostFragment extends Fragment {
    private static final String ARG_DESCRIPTION = "ARG_DESCRIPTION";
    private static final String ARG_PRICE = "ARG_PRICE";
    private static final String ARG_START_DATE = "ARG_START_DATE";
    private static final String ARG_END_DATE = "ARG_END_DATE";
    private static final String ARG_CITY = "ARG_CITY";
    private static final String ARG_URL = "NewSubletImage.png";

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
                             final Bundle savedInstanceState) {
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

        view.findViewById(R.id.btnCancelNewPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDescription.setText("");
                etCity.setText("");
                etEndDate.setText("");
                etStartDate.setText("");
                etPrice.setText("");
                autocompleteFragment.setText("");
                ivImage.setImageResource(R.drawable.ic_launcher);
            }
        });

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

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                Fragment newFr = SingleSubletPostFragment.newInstance(subletPost.getId());
                transaction.replace(R.id.content_frame, newFr);
                transaction.addToBackStack(null);

                transaction.commit();
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

        isInstanceState = true;
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
