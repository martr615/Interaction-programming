package com.example.martin.project_password;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * Created by Martin on 2016-05-30.
 */

//PasswordClass är en klass som visar hur starkt ett lösenord är. Det visas i form
//av färg på text och genom utvecklingen i form av en progressbar.
//Klassen PasswordStrenght innehåller kod som avgör vilkoren av hur starkt ett lösenord är.
//I klassen PasswordStrength finns en variabel som heter maxStrenght. maxStrenght plusas på med 1
//när varje vilkor uppfylls.

public class PasswordClass extends LinearLayout {

    private EditText passField;
    private ProgressBar progressBar;
    private TextView passMessageField;
    private Button enterButton;
    private PasswordClassListener listener;
    private PasswordStrenght passwordStrenght;
    private boolean savePassword = false;

    //konstruktor
    public PasswordClass(Context context){
        super(context);
        init(null, 0);
    }

    public PasswordClass(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs, 0);
    }

    public PasswordClass(Context context, AttributeSet attrs, int defstyle){
        super(context);
        init(attrs, defstyle);
    }

    //En custom lyssnare har skapats för att kunna spara lösenordet
    public interface PasswordClassListener{
        void onPasswordSaved(String password); //när lösenordet kan sparas/
    }


    /**
     * skapa en komponent av password
     * @param attributeSet
     * @param defStyle
     */
    private void init(AttributeSet attributeSet, int defStyle){

        this.listener = null;

        // Layoutinflater gör att vi ta från password_layout istället för activity_main.xml
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.password_layout, this);

        // Objekt från klassen "PasswordStrength".
        passwordStrenght = new PasswordStrenght();

        // De olika komponenterna från "password_layout.xml".
        passField = (EditText)findViewById(R.id.editText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        passMessageField = (TextView) findViewById(R.id.passMessage);
        enterButton = (Button) findViewById(R.id.button);

        // Progressbaren har 4 delar dvs 4 storlekar. Den kan visa utvecklingen i 0%, 25%, 50%, 75% och 100%
        progressBar.setMax(4);

        // En lyssnare för fältet password.
        passField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final String passwordSeguence = s.toString();

                //Om det är tomt i password fältet så ska allt vara standard och standard i vårt fall visar den ingenting.
                if (s.toString().length() == 0) {
                    progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#000000"), android.graphics.PorterDuff.Mode.SRC_ATOP);
                    progressBar.setProgress(0);
                    passMessageField.setText("Please, enter your password.");
                    passMessageField.setTextColor(Color.parseColor("#000000"));
                    savePassword = false;
                } else {

                    //vi räknar ut antalet procent av maxStrenght dvs hur starkt lösenordet är i procent.
                    float passStrength = ((float) passwordStrenght.calcPasswordStrenght(s.toString()));
                    float passPercentage = passStrength / passwordStrenght.getMaxStrenght();

                    //Rita ut progressbaren beroende på antalet procent dvs i klassen PasswordStrenght räknar vi ut
                    //vilka vilkor som är uppfyllda och genom det vet vi hur starkt ett lösenord är.
                    if (passPercentage < 0.20f) { //när inga vilkor är uppfylld
                        progressBar.setProgress(0); //visar progressbaren på 0%
                        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#fdc68a"), android.graphics.PorterDuff.Mode.SRC_ATOP); //färga progressbaren gult
                        passMessageField.setText("Too short Password"); //skriv ut texten "too short password"
                        passMessageField.setTextColor(Color.parseColor("#fdc68a")); //färga texten gul
                        savePassword = false; //inte godkänd lösenord så kan inte sparas
                    } else if (0.20f <= passPercentage && passPercentage < 0.40f) { //om ett vilkor är uppfylld.
                        progressBar.setProgress(1); //uppdatera progressbaren och rita ut nästa steg dvs 25% av längden.
                        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#ebf209"), android.graphics.PorterDuff.Mode.SRC_ATOP); //färga progressbaren ljustgrönt
                        passMessageField.setText("Weak Password"); //skriv ut "weak password"
                        passMessageField.setTextColor(Color.parseColor("#ebf209")); //färga texten ljustgrönt
                        savePassword = false;//inte godkänd lösenord så kan inte sparas

                        //samma princip men för olika värden och vilkor.
                    } else if (0.40f <= passPercentage && passPercentage < 0.60f) {
                        progressBar.setProgress(2);
                        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#42b48e"), android.graphics.PorterDuff.Mode.SRC_ATOP);
                        passMessageField.setText("Okay Password");
                        passMessageField.setTextColor(Color.parseColor("#42b48e"));
                        savePassword = true; //godkänd lösenord för att sparas
                    } else if (0.60f <= passPercentage && passPercentage < 0.80f) {
                        progressBar.setProgress(3);
                        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#b3fe6a"), android.graphics.PorterDuff.Mode.SRC_ATOP);
                        passMessageField.setText("Strong Password");
                        passMessageField.setTextColor(Color.parseColor("#b3fe6a"));
                        savePassword = true;
                    } else if (0.80f <= passPercentage) {
                        progressBar.setProgress(4);
                        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#6ecff6"), android.graphics.PorterDuff.Mode.SRC_ATOP);
                        passMessageField.setText("Very Strong Password");
                        passMessageField.setTextColor(Color.parseColor("#6ecff6"));
                        savePassword = true;
                    }
                }

                //En lyssnare för knappen "Enter"
                enterButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //om lösen är starkt nog dvs savePassword = true så kan vi spara.
                        if (savePassword && listener != null) {
                            listener.onPasswordSaved(passwordSeguence);
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // En lyssnare som tar emot events.
    public void setPasswordClassListener(PasswordClassListener listener) {this.listener = listener;}
}

