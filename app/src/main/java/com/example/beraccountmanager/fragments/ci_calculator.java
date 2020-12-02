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

import java.text.DecimalFormat;


public class ci_calculator extends Fragment {
    private EditText principle,interest,years,time_period;
    private Button cicalc;
    private TextView screen;
    double p,n,r,t,a,total_amount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        principle=getView().findViewById(R.id.principal_amount);
        interest=getView().findViewById(R.id.simple_interest);
        years=getView().findViewById(R.id.number_of_years);
        cicalc=getView().findViewById(R.id.calculate_ci);
        time_period=getView().findViewById(R.id.time_period);
        screen=getView().findViewById(R.id.display);
    }
    public void ButtonClickCI(View v){
        p=Double.parseDouble(principle.getText().toString());
        n=Double.parseDouble(years.getText().toString());
        r=Double.parseDouble(interest.getText().toString());
        t=Double.parseDouble(time_period.getText().toString());
        r=r/100;
        a=Math.pow((1+r/n),(n*t));
        total_amount=a*p;
        DecimalFormat numberFormat =new DecimalFormat("#.000");
        screen.setText(Double.toString(Double.parseDouble(numberFormat.format(total_amount))));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ci_calculator, container, false);
    }
}