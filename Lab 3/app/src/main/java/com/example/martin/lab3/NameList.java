package com.example.martin.lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;


/**
 * Created by Martin on 2016-05-11.
 */



public class NameList extends View { //NameList är en klass som ärver från View. Klassen sköter UI:t.
    private String name;
    private Paint paint;
    private int screenHeight, screenWidth;


     // Konstruktorn.
    public NameList(Context theContext, String theName){
        super(theContext);
        name = theName; //namnet som sätts som ett item

        ///hämta skärmstorlek för att kunna rita ut bred, höjd mm för popup-listan
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) theContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        init();
    }

    public void init(){
        paint = new Paint();
        paint.setTextSize(45f); //Text storlek.
        if(name == InteractiveSearcher.noMatchingName) {
            paint.setColor(Color.RED); // Visa röd text om namnet inte hittas.
        }
    }


     //Kallas automatiskt och ritar ut texten på en canvas.
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawText(name, 10, 40, paint);
    }


    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure){
        this.setMeasuredDimension(screenWidth/2, screenHeight/25); //Använder skärmens storlek för att rita ut texten
    }
}