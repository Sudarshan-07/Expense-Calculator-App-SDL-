package com.example.beraccountmanager.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.beraccountmanager.R;
import com.example.beraccountmanager.fragments.AboutUSFragment;
import com.example.beraccountmanager.fragments.CICalculatorFragment;
import com.example.beraccountmanager.fragments.CalculatorFragment;
import com.example.beraccountmanager.fragments.CategoryFragment;
import com.example.beraccountmanager.fragments.ExpenseReportFragment;
import com.example.beraccountmanager.fragments.SICalculatorFragment;
import com.example.beraccountmanager.fragments.TodayFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    String URL = "https://economictimes.indiatimes.com/wealth/invest/top-10-investment-options/articleshow/64066079.cms";

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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        closeNavigationDrawer();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (!closeNavigationDrawer()) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (!(currentFragment instanceof TodayFragment)) {
                loadTodayFragment();
            } else {
                super.onBackPressed();
            }
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
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
                break;
            case R.id.SI_calc:
                loadFragment(SICalculatorFragment.class, menuItem.getItemId(), menuItem.getTitle());
                break;
            case R.id.CI_calc:
                loadFragment(CICalculatorFragment.class, menuItem.getItemId(), menuItem.getTitle());
                break;
            case R.id.exp_categories:
                loadFragment(CategoryFragment.class, menuItem.getItemId(), menuItem.getTitle());
                break;
            case R.id.nav_io:
                openBrowser();
                break;
            case R.id.nav_contact:
                loadFragment(AboutUSFragment.class, menuItem.getItemId(), menuItem.getTitle());
                break;
            default:
                loadFragment(TodayFragment.class, menuItem.getItemId(), menuItem.getTitle());
        }
    }

    private void openBrowser() {
        if (!URL.startsWith("http://") && !URL.startsWith("https://"))
            URL = "http://" + URL;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(browserIntent);
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
}

