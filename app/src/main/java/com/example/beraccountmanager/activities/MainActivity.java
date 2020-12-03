package com.example.beraccountmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.beraccountmanager.R;
import com.example.beraccountmanager.fragments.CalculatorFragment;
import com.example.beraccountmanager.fragments.ExpenseReportFragment;
import com.example.beraccountmanager.fragments.SICalculatorFragment;
import com.example.beraccountmanager.fragments.TodayFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavDrawer;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavDrawer = (NavigationView) findViewById(R.id.hamburger_menu);
        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerContent(mNavDrawer);
        loadTodayFragment();
    }

    @Override
    public void onBackPressed() {
        if (!closeNavigationDrawer()) {
            Fragment currentFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.content_frame);
            if (!(currentFragment instanceof TodayFragment)) {
                loadTodayFragment();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void loadFragment(Class fragmentClass, @IdRes int navDrawerCheckedItemId,
                              CharSequence toolbarTitle) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        insertFragment(fragment);
        mNavDrawer.setCheckedItem(navDrawerCheckedItemId);
        setTitle(toolbarTitle);
    }

    private void loadTodayFragment() {
        loadFragment(TodayFragment.class, R.id.nav_home,
                getResources().getString(R.string.group1_Home));
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }

                });
    }

    private boolean closeNavigationDrawer() {
        boolean drawerIsOpen = mDrawerLayout.isDrawerOpen(GravityCompat.START);
        if (drawerIsOpen) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return drawerIsOpen;
    }

    public void hideNavigationBar() {
        closeNavigationDrawer();
    }

    private void selectDrawerItem(MenuItem menuItem) {
        closeNavigationDrawer();
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                loadFragment(TodayFragment.class, menuItem.getItemId(), menuItem.getTitle());
                break;
            case R.id.nav_reports:
                loadFragment(ExpenseReportFragment.class, menuItem.getItemId(), menuItem.getTitle());
                break;
            case R.id.simple_calc:
                loadFragment(CalculatorFragment.class, menuItem.getItemId(), menuItem.getTitle());
            case R.id.SI_calc:
                loadFragment(SICalculatorFragment.class, menuItem.getItemId(), menuItem.getTitle());
                break;
            /// case R.id.nav_sett:
            //startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            //break;
            default:
                loadFragment(TodayFragment.class, menuItem.getItemId(), menuItem.getTitle());
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
    }

}

