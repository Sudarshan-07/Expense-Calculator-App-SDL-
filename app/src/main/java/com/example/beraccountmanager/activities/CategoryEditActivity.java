package com.example.beraccountmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.beraccountmanager.fragments.AddTransactionFragment;
import com.example.beraccountmanager.fragments.CategoryEditFragment;

public class CategoryEditActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        insertFragment(new CategoryEditFragment());
    }
}