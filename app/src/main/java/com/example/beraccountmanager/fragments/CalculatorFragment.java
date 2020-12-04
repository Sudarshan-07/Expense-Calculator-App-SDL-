package com.example.beraccountmanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.beraccountmanager.R;


public class CalculatorFragment extends Fragment {
    private TextView screen;
    private Button del,result,add,mul,div,sub,percent,one,two,three,four,five,six,seven,eight,nine,zero,dot,ac,power;
    private String input,answer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen=getView().findViewById(R.id.display);
        ac=getView().findViewById(R.id.allclear);
        power=getView().findViewById(R.id.power);
        del=getView().findViewById(R.id.buttonDel);
        result=getView().findViewById(R.id.buttoneql);
        add=getView().findViewById(R.id.button_add);
        mul=getView().findViewById(R.id.buttonmul);
        div=getView().findViewById(R.id.buttondiv);
        sub=getView().findViewById(R.id.buttonsub);
        percent=getView().findViewById(R.id.percent);
        one=getView().findViewById(R.id.button1);
        two=getView().findViewById(R.id.button2);
        three=getView().findViewById(R.id.button3);
        four=getView().findViewById(R.id.button4);
        five=getView().findViewById(R.id.button5);
        six=getView().findViewById(R.id.button6);
        seven=getView().findViewById(R.id.button7);
        eight=getView().findViewById(R.id.button8);
        nine=getView().findViewById(R.id.button9);
        zero=getView().findViewById(R.id.button0);
        dot=getView().findViewById(R.id.buttonDot);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.simple_calculator, container, false);
        return rootView;

    }
    public void ButtonClick(View view){
        Button button=(Button) view;
        String data=button.getText().toString();
        switch(data){
            case "AC":
                input="";
                break;
            case"x":
                Solve();
                input+="*";
                break;
            case "^":
                Solve();
                input+="^";
                break;
            case "=":
                Solve();
                answer=input;
                break;
            case "BACK":
                String newText=input.substring(0,input.length()-1);
                input=newText;
                break;
            default:
                if(input==null){
                    input="";
                }
                if(data.equals("+") || data.equals("-") || data.equals("/")){
                    Solve();
                }
                input+=data;
        }
        screen.setText(input);
    }
    private void Solve(){
        if(input.split("\\*").length==2) {
            String number[] = input.split("\\*");
            try {
                double mul = Double.parseDouble(number[0]) * Double.parseDouble(number[1]);
                input = mul+"";
            }catch(Exception e){
                //error
            }
        }
        else if(input.split("/").length==2) {
            String number[] = input.split("/");
            try {
                double div = Double.parseDouble(number[0]) / Double.parseDouble(number[1]);
                input = div+"";
            }catch(Exception e){
                //error
            }
        }
        else if(input.split("\\+").length==2) {
            String number[] = input.split("\\+");
            try {
                double addition = Double.parseDouble(number[0]) + Double.parseDouble(number[1]);
                input =addition+"";
            }catch(Exception e){
                //error
            }
        }
        else if(input.split("-").length>1) {
            String number[] = input.split("-");
            //to subtract from negative number
            if(number[0]=="" && number.length==2){
                number[0]=0+"";
            }
            try {
                double subtraction = 0;
                if(number.length==2){
                    subtraction=Double.parseDouble(number[0])-Double.parseDouble(number[1]);
                }
                else if(number.length==3){
                    subtraction=Double.parseDouble((number[1]))-Double.parseDouble(number[2]);
                }
                input = subtraction+"";
            }catch(Exception e){
                //error
            }
        }
        else if(input.split("\\^").length==2) {
            String number[] = input.split("\\^");
            try {
                double pow = Math.pow(Double.parseDouble(number[0]),Double.parseDouble(number[1]));
                input = pow+"";
            }catch(Exception e){
                //error
            }
        }
        else if(input.split("%").length==2) {
            String number[] = input.split("%");
            try {
                double per = Double.parseDouble(String.valueOf(number))/100;
                input = per+"";
            }catch(Exception e){
                //error
            }
        }
        //to remove last digit .0 from integer result
        String n[]=input.split("\\.");
        if(n.length>1){
            if(n[1].equals("0")){
                input=n[0];
            }
        }
        screen.setText(input);
    }


}