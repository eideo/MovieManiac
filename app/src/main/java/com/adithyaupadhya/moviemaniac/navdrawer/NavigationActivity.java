package com.adithyaupadhya.moviemaniac.navdrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adithyaupadhya.database.DBConstants;
import com.adithyaupadhya.database.sharedpref.AppPreferenceManager;
import com.adithyaupadhya.moviemaniac.R;
import com.adithyaupadhya.moviemaniac.base.ImageDialogFragment;
import com.adithyaupadhya.moviemaniac.base.Utils;
import com.adithyaupadhya.moviemaniac.base.interfaces.OnFragmentBackPress;
import com.adithyaupadhya.moviemaniac.base.interfaces.OnImageClickListener;
import com.adithyaupadhya.moviemaniac.celebrity.CelebrityLaunchFragment;
import com.adithyaupadhya.moviemaniac.games.GameSplashScreenActivity;
import com.adithyaupadhya.moviemaniac.login.SignInActivity;
import com.adithyaupadhya.moviemaniac.movies.MovieLaunchFragment;
import com.adithyaupadhya.moviemaniac.support.SupportDeveloperActivity;
import com.adithyaupadhya.moviemaniac.tvseries.TVSeriesLaunchFragment;
import com.adithyaupadhya.newtorkmodule.volley.networkconstants.AppIntentConstants;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnImageClickListener, MaterialDialog.SingleButtonCallback {

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private int mSelectedNavItem;
    private OnFragmentBackPress mBackPressListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // APIConstants.initializeLanguageGenreHashTable(this);
        initializeNavigationDrawer(savedInstanceState != null);
    }

    private void initializeNavigationDrawer(boolean isRelaunch) {
        AppPreferenceManager preferences = AppPreferenceManager.getAppPreferenceInstance(this);

        mFragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = mNavigationView.getHeaderView(0);
//        ImageView imageView = (ImageView) headerView.findViewById(R.id.circularImageView);
//        imageView.setImageUrl(preferences.getPreferenceData(DBConstants.USER_PROFILE_PIC), VolleySingleton.getInstance().getVolleyImageLoader());
        Glide.with(this)
                .load(preferences.getPreferenceData(DBConstants.USER_PROFILE_PIC))
                .placeholder(R.drawable.vector_default_person)
                .dontAnimate()
                .into((ImageView) headerView.findViewById(R.id.circularImageView));

        ((TextView) (headerView.findViewById(R.id.textViewName))).setText(preferences.getPreferenceData(DBConstants.USER_NAME));
        ((TextView) (headerView.findViewById(R.id.textViewEmail))).setText(preferences.getPreferenceData(DBConstants.USER_EMAIL));

        mNavigationView.setNavigationItemSelectedListener(this);
        if (!isRelaunch) {
            mSelectedNavItem = R.id.nav_movie;
            mFragmentManager.beginTransaction().add(R.id.fragment_container, new MovieLaunchFragment()).commit();
        }
    }


    public void buildActivityDrawerAndToolbar(Toolbar toolbar) {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Glide.with(NavigationActivity.this)
                        .load(AppPreferenceManager.getAppPreferenceInstance(NavigationActivity.this)
                                .getPreferenceData(DBConstants.USER_PROFILE_PIC))
                        .placeholder(R.drawable.vector_default_person)
                        .dontAnimate()
                        .into((ImageView) drawerView.findViewById(R.id.circularImageView));
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        final int NAV_DRAWER_DELAY = 250;
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean resetPreviouslySelectedItem = false;
                FragmentTransaction transaction = mFragmentManager.beginTransaction();

                if (item.getItemId() != mSelectedNavItem) {
                    switch (item.getItemId()) {
                        case R.id.nav_movie:
                            transaction.replace(R.id.fragment_container, new MovieLaunchFragment()).commit();
                            break;

                        case R.id.nav_tvseries:
                            transaction.replace(R.id.fragment_container, new TVSeriesLaunchFragment()).commit();
                            break;

                        case R.id.nav_celebrity:
                            transaction.replace(R.id.fragment_container, new CelebrityLaunchFragment()).commit();
                            break;

                        case R.id.nav_game:
                            resetPreviouslySelectedItem = true;
                            startActivity(new Intent(NavigationActivity.this, GameSplashScreenActivity.class));
                            break;

                        case R.id.nav_support:
                            resetPreviouslySelectedItem = true;
                            startActivity(new Intent(NavigationActivity.this, SupportDeveloperActivity.class));
                            break;

                        case R.id.nav_rateapp:
                            resetPreviouslySelectedItem = true;
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException exception) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                            break;

                        case R.id.nav_bug:
                            resetPreviouslySelectedItem = true;
                            String personalDetails = "Manufacturer : " + Build.MANUFACTURER +
                                    "\nDevice Model : " + Build.MODEL +
                                    "\nAndroid OS version : " + Build.VERSION.RELEASE +
                                    "\nSDK version : " + Build.VERSION.SDK_INT +
                                    "\n\nPLEASE NOTE: Device data is used only for Debugging purposes. Your identity is completely safe :)" +
                                    "\n++++++++++++++++++++++++++++++++++++++\n\n";

                            try {
                                Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "developer.moviemaniac@gmail.com"));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MovieManiac: Raise a bug / Request new features");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, personalDetails);
                                startActivity(emailIntent);
                            } catch (Exception exception) {
                                Toast.makeText(NavigationActivity.this, "Error: No email client found in your device...", Toast.LENGTH_LONG).show();
                            }
                            break;

                        case R.id.nav_logout:
                            Utils.showLogoutMaterialDialog(NavigationActivity.this, NavigationActivity.this);
                            resetPreviouslySelectedItem = true;
                            break;
                    }

                    if (resetPreviouslySelectedItem) {
                        ((NavigationView) (findViewById(R.id.nav_view))).setCheckedItem(mSelectedNavItem);
                    } else {
                        mSelectedNavItem = item.getItemId();
                    }
                }
            }
        }, NAV_DRAWER_DELAY);

        return true;
    }

    @Override
    public void onImageClick(String url) {
        ImageDialogFragment dialogFragment = new ImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppIntentConstants.BUNDLE_URL, url);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(mFragmentManager, "ImageDialog");
    }

    public void setOnFragmentBackPressListener(OnFragmentBackPress listener) {
        mBackPressListener = listener;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (mBackPressListener != null)
            mBackPressListener.handleFragmentSearchView();
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(MaterialDialog dialog, DialogAction which) {
        // Facebook Logout
        LoginManager.getInstance().logOut();
        AppPreferenceManager.getAppPreferenceInstance(this).clearAllUserPreferenceData();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBackPressListener = null;
    }
}
