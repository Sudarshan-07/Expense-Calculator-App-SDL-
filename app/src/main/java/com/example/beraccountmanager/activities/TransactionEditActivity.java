package com.example.beraccountmanager.activities;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import com.example.beraccountmanager.fragments.AddTransactionFragment;

public class TransactionEditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insertFragment(new AddTransactionFragment());
        setupActionBar();
    }
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar (toolbar).
        }
    }
}