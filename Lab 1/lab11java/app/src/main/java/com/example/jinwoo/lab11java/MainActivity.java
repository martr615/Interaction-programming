package com.example.jinwoo.lab11java;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Declaring all objects.
        LinearLayout layout = new LinearLayout(this);
        Button theButton = new Button(this);
        EditText textField  = new EditText(this);
        EditText multiLineText  = new EditText(this);
        RatingBar rating = new RatingBar(this);

        // Set text of objects.
        theButton.setText("Knapp");
        textField.setText("Textfält");
        multiLineText.setText("Ett fält som klarar....");

        // Modifying the objects.
        textField.setSingleLine(); // The first text field is a single line.
        layout.setOrientation(LinearLayout.VERTICAL);

        // Layout parameters.
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        // Button parameters.
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Text field parameters.
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Rating bar parameters.
        LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Multiline text field parameters.
        LinearLayout.LayoutParams multiLineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        multiLineParams.weight = 1.0f; // Need to add weight to the multiline text field so it goes down to the bottom of the screen.

        // Set layout parameters.
        layout.setLayoutParams(layoutParams);

        // Add all objects to the layout.
        layout.addView(theButton, buttonParams);
        layout.addView(textField, textParams);
        layout.addView(rating, ratingParams);
        layout.addView(multiLineText, multiLineParams);

        setContentView(layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
