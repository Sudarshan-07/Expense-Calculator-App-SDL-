package com.example.beraccountmanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beraccountmanager.R;

import java.text.DecimalFormat;


public class CICalculatorFragment extends Fragment {
    private EditText principle,interest,years,time_period;
    private Button cicalc;
    private TextView screen;
    double p,n,r,t,a,total_amount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ci_calculator, container, false);
        principle=rootView.findViewById(R.id.principal_amount);
        interest=rootView.findViewById(R.id.simple_interest);
        years=rootView.findViewById(R.id.number_of_years);
        cicalc=rootView.findViewById(R.id.calculate_ci);
        time_period=rootView.findViewById(R.id.time_period);
        screen=rootView.findViewById(R.id.display);
        cicalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    p = Double.parseDouble(principle.getText().toString());
                    n = Double.parseDouble(years.getText().toString());
                    r = Double.parseDouble(interest.getText().toString());
                    t = Double.parseDouble(time_period.getText().toString());
                    r = r / 100;
                    a = Math.pow((1 + r / n), (n * t));
                    total_amount = a * p;
                    DecimalFormat numberFormat = new DecimalFormat("#.000");
                    screen.setText(Double.toString(Double.parseDouble(numberFormat.format(total_amount))));
                }catch(Exception e){
                    //Toggle error
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}