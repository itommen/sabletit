package com.example.ndt.sabletid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ndt.sabletid.Models.User.User;
import com.example.ndt.sabletid.ViewModels.UserViewModel;
import com.google.android.gms.maps.SupportMapFragment;

public class MasterDetailActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        userViewModel.getConnectedUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                Fragment newFragment;
                int relevantMenu;

                if(user == null) {
                    newFragment = new LoginFragment();
                    relevantMenu = R.menu.guest_drawer_view;
                }
                else {
                    newFragment = new UserDetailsFragment();
                    relevantMenu = R.menu.user_drawer_view;
                }

                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.getMenu().clear();
                navigationView.inflateMenu(relevantMenu);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();

                        Fragment newFragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_login: {
                                newFragment = new LoginFragment();
                                break;
                            }

                            case R.id.menu_register: {
                                newFragment = new RegisterFragment();
                                break;
                            }

                            case R.id.menu_edit_details: {
                                newFragment = new UserDetailsFragment();
                                break;
                            }

                            case R.id.menu_create_sublet: {
                                newFragment = new CreateNewSubletPostFragment();
                                break;
                            }

                            case R.id.menu_map: {
                                newFragment = new MapsActivity();
                                break;
                            }

                            case R.id.menu_logout: {
                                userViewModel.logout();
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                transaction.replace(R.id.content_frame, new LoginFragment());
                                transaction.addToBackStack(null);

                                transaction.commit();
                            }
                        }

                        if(newFragment != null) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                            transaction.replace(R.id.content_frame, newFragment);
                            transaction.addToBackStack(null);

                            transaction.commit();
                        }

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((DrawerLayout) findViewById(R.id.drawer_layout)).openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
