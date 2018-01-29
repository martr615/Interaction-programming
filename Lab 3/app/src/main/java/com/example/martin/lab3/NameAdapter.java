package com.example.martin.lab3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Martin on 2016-05-11.
 */



//NameAdapter skapar items i form av Strings som består av namnen av klassen "NameList".
public class NameAdapter extends BaseAdapter { //NameAdapter är en adapter som ärver från BaseAdapter.
    Context context;
    private List<String> nameList;


    //Konstruktor som tar emot "context" och listan med alla namnförslag.
    public NameAdapter(Context theContext, List<String> theNameList) {
        context = theContext;
        nameList = theNameList; //namelist är en lista av string som har alla namnförslag
    }


    //Ta storleken på listan med alla namnförslag.
    @Override
    public int getCount() {
        if (nameList == null) return 0;
        return nameList.size(); //Antal namn som finns i listan med alla namnförslag
    }


    //Skapar instanser av NameList.
    @Override
    public View getView(int index, View v, ViewGroup viewGroup) { //index = sökindex, v = vyn,
        return new NameList(context, nameList.get(index)); //retunera en instans av namelist
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }
}