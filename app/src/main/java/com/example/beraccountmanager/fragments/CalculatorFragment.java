package com.example.beraccountmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.beraccountmanager.R;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class CalculatorFragment extends Fragment {
    private TextView input_screen,output_screen;
    private Button equal,back,add,mul,div,sub,percent,one,two,three,four,five,six,seven,eight,nine,zero,dot,ac,bracket;
    private String process;
    boolean checkBracket=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.simple_calculator, container, false);

        one=rootView.findViewById(R.id.button1);
        two=rootView.findViewById(R.id.button2);
        three=rootView.findViewById(R.id.button3);
        four=rootView.findViewById(R.id.button4);
        five=rootView.findViewById(R.id.button5);
        six=rootView.findViewById(R.id.button6);
        seven=rootView.findViewById(R.id.button7);
        eight=rootView.findViewById(R.id.button8);
        nine=rootView.findViewById(R.id.button9);
        zero=rootView.findViewById(R.id.button0);

        add=rootView.findViewById(R.id.buttonPlus);
        mul=rootView.findViewById(R.id.buttonMul);
        div=rootView.findViewById(R.id.buttonDiv);
        sub=rootView.findViewById(R.id.buttonMinus);


        ac=rootView.findViewById(R.id.allClear);
        equal=rootView.findViewById(R.id.buttoneql);
        percent=rootView.findViewById(R.id.percent);
        dot=rootView.findViewById(R.id.buttonDot);
        bracket=rootView.findViewById(R.id.brackets);
        back=rootView.findViewById(R.id.backspace);

        input_screen=rootView.findViewById(R.id.display1);
        output_screen=rootView.findViewById(R.id.display2);

        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_screen.setText("");
                output_screen.setText("");
            }
        });


        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "0");
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "6");
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "8");
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "9");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "+");
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "-");
            }
        });


        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "*");
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "/");
            }
        });

        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + ".");
            }
        });

        percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();
                input_screen.setText(process + "%");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=input_screen.getText().toString();
                if(str.length()>1) {
                    str = str.substring(0, str.length() - 1);
                    input_screen.setText(str);
                    output_screen.setText("");
                }else if (str.length()<=1){
                    input_screen.setText("");
                    output_screen.setText("");
                }
            }
        });

        bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBracket){
                    process = input_screen.getText().toString();
                    input_screen.setText(process + ")");
                    checkBracket = false;
                }else{
                    process = input_screen.getText().toString();
                    input_screen.setText(process + "(");
                    checkBracket = true;
                }

            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = input_screen.getText().toString();

                process = process.replaceAll("%","/100");

                Context rhino = Context.enter();

                rhino.setOptimizationLevel(-1);

                String finalResult = "";

                try {
                    Scriptable scriptable = rhino.initStandardObjects();
                    finalResult = rhino.evaluateString(scriptable,process,"javascript",1,null).toString();
                }catch (Exception e){
                    finalResult="0";
                }
                //to remove last digit .0 from integer result
                String n[]=finalResult.split("\\.");
                if(n.length>1){
                    if(n[1].equals("0")){
                        finalResult=n[0];
                    }
                }

                output_screen.setText(finalResult);
            }
        });

        return rootView;

    }

}