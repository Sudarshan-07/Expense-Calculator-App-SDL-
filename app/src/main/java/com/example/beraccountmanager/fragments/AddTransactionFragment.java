package com.example.beraccountmanager.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.example.beraccountmanager.R;

public class AddTransactionFragment extends Fragment {

    private EditText expense_add_edit_value,transaction_date;
    private AppCompatSpinner choose_category_spinner;
    private ProgressBar select_cat_progress_bar;
    private RadioGroup radio_group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.add_transaction_fragment, container, false);
        expense_add_edit_value = (EditText) rootView.findViewById(R.id.expense_add_edit_value);
        choose_category_spinner = (AppCompatSpinner) rootView.findViewById(R.id.choose_category_spinner);
        radio_group = rootView.findViewById(R.id.radio_group);
        select_cat_progress_bar = rootView.findViewById(R.id.select_cat_progress_bar);

        setEditTextDefaultValue();

        expense_add_edit_value.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    checkValueFieldForIncorrectInput();
                    return true;
                }
                return false;
            }
        });
       choose_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setEditTextDefaultValue() {
        expense_add_edit_value.setText(String.valueOf(0));
        expense_add_edit_value.selectAll();
    }

    private boolean checkValueFieldForIncorrectInput() {
        String etValue = expense_add_edit_value.getText().toString();
        try {
            if (etValue.length() == 0) {
                expense_add_edit_value.setError(getResources().getString(R.string.error_empty_field));
                return false;
            } else if (Float.parseFloat(etValue) == 0.00f) {
              expense_add_edit_value.setError(getResources().getString(R.string.error_zero_value));
                return false;
            }
        } catch (Exception e) {
            expense_add_edit_value.setError(getResources().getString(R.string.error_incorrect_input));
            return false;
        }
        return true;
    }
}