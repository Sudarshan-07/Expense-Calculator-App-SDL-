package com.example.beraccountmanager.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.beraccountmanager.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.base_fragment_actvity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    protected void insertFragment(Fragment fragment) {
           //Insert the fragment by replacing the existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

}
