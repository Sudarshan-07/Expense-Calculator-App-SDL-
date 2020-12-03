package com.example.beraccountmanager.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.beraccountmanager.fragments.AddTransactionFragment;
import com.example.beraccountmanager.fragments.CategoryEditFragment;

public class CategoryEditActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        insertFragment(new CategoryEditFragment());
        setupActionBar();
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // to Show the Up button in the toolbar
        }
    }

}