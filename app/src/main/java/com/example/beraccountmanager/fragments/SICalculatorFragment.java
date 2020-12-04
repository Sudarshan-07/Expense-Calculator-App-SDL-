package com.example.beraccountmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beraccountmanager.R;


public class SICalculatorFragment extends Fragment {
    private EditText principle,interest,years;
    private Button sicalc;
    private TextView screen;
    double p,n,r,total_amount,total_interest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.simple_interest_calculator, container, false);
        principle=rootView.findViewById(R.id.principal_amount);
        interest=rootView.findViewById(R.id.simple_interest);
        years=rootView.findViewById(R.id.number_of_years);
        sicalc=rootView.findViewById(R.id.calculate_si);
        screen=rootView.findViewById(R.id.display);
        sicalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p=Double.parseDouble(principle.getText().toString());
                n=Double.parseDouble(years.getText().toString());
                r=Double.parseDouble(interest.getText().toString());
                total_interest=(p*n*r)/100;
                total_amount=(p+total_interest);
                screen.setText(Double.toString(total_amount));
            }
        });
        return rootView;
    }

}