package com.example.martin.project_accountreg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    public Hashtable<String, String> accountInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final AccountReg accountReg = new AccountReg(this, 4); //skapa klassen AccountReg
        accountReg.addingField("Name", false); //lägger ill en fält som gör att man kan skriva in sitt namn, den är inte obligatorisk därav = false
        accountReg.addingEmailField(true); //lägger till en fält som gör att man kan skriva in sin mail, den är obligatoriskt
        accountReg.addingPasswordField(true); //lägger till en fält som gör att man kan bestämma ett lösenord
        accountReg.addingNumericField("Age", false); //lägger till en fält som gör att man ka fylla i sitt namn
        accountReg.addingRegisterButton(); //lägger till en knapp som gör att man kan registera sin information

        //I accountReg har jag skapat en custom lyssnare.
        accountReg.setmustFillInFieldsListener(new AccountReg.mustFillInFieldsListener() {
            @Override
            public void onAccountInfoSaved(Hashtable<String, String> theAccountInfo) {
                accountInfo = theAccountInfo;
            }
        });

        setContentView(accountReg.getAccountRegForm());
    }
}
