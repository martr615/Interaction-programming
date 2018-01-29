package com.example.martin.project_accountreg;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Hashtable;

/**
 * Created by Martin on 2016-05-16.
 */

//AccountReg är en klass som skapar registeringsformuläret.
//I koden har 4 formulär skapats som kan fyllas i.
//Klassen är även programmerad så att det finns obligatoriska fält
//som måste fyllas i för att kunna registera sig.
//Det finns sedan en register knapp och när den knappen används
//så sparas alla ifyllda formulär i en Hashtable.

public class AccountReg extends LinearLayout{
    //variablar för UI:t
    private LinearLayout linearLayout;
    private TextView errorMes, regSuccesfully;


    //Variablar för indata.
    private Hashtable<String, String> allData;
    private Hashtable<String, Integer> numericAccountData;
    private int numberFields;
    private int counterMustFillInFields = 0;


    //variable för custom lyssnare
    private mustFillInFieldsListener listener;

    public AccountReg(Context context, int theNumberFields){
        super(context);
        numberFields = theNumberFields + 1; // +1 behövs för att kunna visa felmeddelandet.
        init();
    }

    //interface = custom lyssnare
    //lyssnaren har skapats för att spara de ifyllda informationerna när det går att spara
    //dvs när de obligatoriska fälten är ifyllda.
    public interface mustFillInFieldsListener{
            void onAccountInfoSaved(Hashtable<String, String> accountInfo); //när informationen kan sparas
    }

    private void init(){
        //Linear layout parameters. Alla fält kommer att läggas in i denna linear layout.
        linearLayout = new LinearLayout(getContext());
        ViewGroup.LayoutParams linearLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setHorizontalGravity(Gravity.CENTER);

        //fel meddelande i form av textView
        errorMes = new TextView(getContext());
        errorMes.setGravity(Gravity.CENTER);
        linearLayout.addView(errorMes);

        //för att visa att regiseringen lyckas, läggs till i view under register knappen.
        regSuccesfully = new TextView(getContext());
        regSuccesfully.setGravity(Gravity.CENTER);

        allData = new Hashtable<String, String>();
        numericAccountData = new Hashtable<String, Integer>();

    }

    public LinearLayout getAccountRegForm(){
        return this.linearLayout;
    }

    /**
     * Ta informatinen i formuläret för att spara
     * @return hashtable med account informationen
     */
    public Hashtable getAccountData(){return this.allData;}

    /**
     * skapa ett fält och väljer ett fält för att bestämma om den är obligatoriskt.
     * @param label etiketten som visas till vänster om editText
     * @param mustFill om fältet är obligatoriskt
     */
    public void addingField(String label, boolean mustFill){
        FieldClass fieldClass= new FieldClass(getContext(), label, mustFill);
        linearLayout.addView(fieldClass);
        if(mustFill) counterMustFillInFields++;
    }

    /**
     * Skapa ett fält som ta emot siffror så som ålder
     * @param label etiketten som visas till vänster om editText
     * @param mustFill om fältet är obligatoriskt
     */

    public void addingNumericField(String label, boolean mustFill){
        FieldClass numericRegistrationField = new FieldClass(getContext(), label, mustFill, true);
        linearLayout.addView(numericRegistrationField);
        if(mustFill) counterMustFillInFields++;
    }

    /**
     * Skapa ett fält för email
     * @param mustFill om fältet är obligatoriskt
     */
    public void addingEmailField(boolean mustFill){
        FieldClass emailField = new FieldClass(getContext(), "Email", mustFill);
        linearLayout.addView(emailField);
        if(mustFill) counterMustFillInFields++;
    }

    /**
     * Skapa ett fält för lösenord
     * @param mustFill om fältet är obligatoriskt
     */
    public void addingPasswordField(boolean mustFill){
        FieldClass passwordField = new FieldClass(getContext(), "Password", mustFill);
        linearLayout.addView(passwordField);
        if(mustFill) counterMustFillInFields++;
    }

    /**
     * Skapa en knapp för att spara informationen av kontot. När vi trycke på knappen
     * så sparas alla infyllda fält på en Hashtable
     */
    public void addingRegisterButton(){
        Button registerButton = new Button(getContext());
        registerButton.setText("Register");

        //om register button klickas.
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int nrMandatoryFieldsFilled = 0;

                //Loopa igenom alla fält och ta informationen på fälterna
                //för att spara informationen om alla obligatoriska fälten är infyllda
                for (int i = 1; i < numberFields; i++) {
                    FieldClass tempRegField = ((FieldClass) linearLayout.getChildAt(i));

                    //Om ett obligatoriskt fält har blivit ifylld så plusar vi på countern till de obligatriska fälten.
                    if (tempRegField.getInputComplete()) nrMandatoryFieldsFilled++;

                    //Om alla obligatoriska fälten är ifyllda.
                    if (nrMandatoryFieldsFilled == counterMustFillInFields) {
                        //spara informationen.
                        for (int k = 1; k < numberFields; k++) {
                            FieldClass tempField = ((FieldClass) linearLayout.getChildAt(k));
                            EditText tempEditText = (EditText) tempField.findViewById(R.id.editText);
                            TextView tempTextView = (TextView) tempField.findViewById(R.id.textView);
                            System.out.println(tempEditText.getText().toString());

                            //Spara texten från EditText.
                            String key = tempTextView.getText().toString();
                            String data = tempEditText.getText().toString();
                            allData.put(key, data);
                            System.out.println("accountData[" + key + "] = " + data);

                            //Aktivera lyssnaren och skickar konto informationen.
                            if (listener != null) {
                                listener.onAccountInfoSaved(allData);
                            }

                            //Uppdatera meddelande fältet när registering har lyckats.
                            errorMes.setText("");   // Då ska det inte finnas felmeddelande.
                            regSuccesfully.setText("Registration successful");
                            regSuccesfully.setTextColor(Color.GREEN);
                        }
                    } else {
                        //Uppdatera meddelande fältet när det inte går att registera sig.
                        errorMes.setText("The required fields need to be filled."); //felmeddelande dyker upp
                        errorMes.setTextColor(Color.RED);
                        regSuccesfully.setText("Registration failed.");
                        regSuccesfully.setTextColor(Color.RED);
                    }
                }
            }
        });

        //lägger till en textview för att visa om registeringen lyckas eller inte.
        linearLayout.addView(registerButton);
        linearLayout.addView(regSuccesfully);
    }

    /**
     * Spara informationen om alla obligatoriska fälten är infyllda.
     * @param listener
     */
    public void setmustFillInFieldsListener(mustFillInFieldsListener listener) {this.listener = listener;}
}
