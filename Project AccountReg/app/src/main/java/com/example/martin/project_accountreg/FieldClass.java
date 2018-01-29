package com.example.martin.project_accountreg;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Martin on 2016-05-16.
 */


//FieldClass är en klass som skapar en enskild fält. Det kan vara allt ifrån mail, lösenord och ålder.
//Dessa fält sätts som obligatoriska eller inte.

public class FieldClass extends LinearLayout {
    private TextView textView;
    private EditText editText;
    private boolean mustFillIn;
    private boolean inputCompl;
    boolean isEmail;
    boolean isPassword;
    boolean isNumeric;


    public FieldClass(Context context, String label, boolean mustFill){
        super(context);
        mustFillIn = mustFill;
        if(mustFill){
            label = label + "*"; //Lägg till "*" efter ettiketen om fälten är obligatoriskt
        }
        isNumeric = false;
        init(label);
    }

    public FieldClass(Context context, String label, boolean mustFill, boolean numeric){
        super(context);
        isNumeric = numeric;
        mustFillIn = mustFill;
        if(mustFill){
            label = label + "*";
        }
        init(label);
    }

    //vi använder oss av account_registration_layout som våran xml.
    private void init(String label){
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.account_registration_layout, this);

    //De olika komponenterna från våran fil account_registration_layout.xml
    editText = (EditText)findViewById(R.id.editText);
    textView = (TextView) findViewById(R.id.textView);

    //Sätter etiketter.
    textView.setText(label);

    //En koll om det är EditText för lösenord, email eller ådlder.
    if(label.equals("Password" )|| label.equals("Password*")){
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //sätter tangentbordet till variation
        isPassword = true; //sätter lösenordet till obligatorisk fält
    }
    else if(label.equals("Email")|| label.equals("Email*")){
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        isEmail = true;
    }
    else if(isNumeric){
        editText.setInputType(InputType.TYPE_CLASS_NUMBER); //Gör så att tangentbordet är numeriska
    }
        // Lyssnare för EditText.
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Om det är textfält.
                if (!isNumeric) {
                    //Om fältet har fått rätta inputs.
                    if (s.length() > 0 && mustFillIn && !isNumeric) {
                        inputCompl = true;
                        if (isEmail && s.toString().matches(".*[@].*")) { //Kolla om det är email och då behövs "@"
                            inputCompl = true;                            //för att det ska räknas som godkänt i email fältet
                        } else {
                            inputCompl = false;
                        }
                        //Kolla om det är lösenord
                        if (isPassword) {
                            inputCompl = true;
                        }
                    } else {
                        inputCompl = false;
                    }
                }
                if (isNumeric) {
                    if (s.length() > 0 && isNumeric && mustFillIn) {
                        inputCompl = true;
                    } else if (s.length() == 0) {
                        inputCompl = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Retunera en bool som säger om de obligatoriska fälten är ifyllda.
     * @return boolean "inputComplete" som berättar om obligatoriska fälten är ifylld.
     */
    public boolean getInputComplete(){return this.inputCompl;}

    }

