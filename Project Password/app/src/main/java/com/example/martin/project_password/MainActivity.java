package com.example.martin.project_password;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {

    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        PasswordClass passwordClass = new PasswordClass(this);
        linearLayout.addView(passwordClass);

        //en lyssnare har skapats i klassen PasswordClasss för att kunna spara lösenordet
        passwordClass.setPasswordClassListener(new PasswordClass.PasswordClassListener() {
            @Override
            public void onPasswordSaved(String thePassword) {
                password = thePassword;
            }
        });

        setContentView(linearLayout);
    }
}


