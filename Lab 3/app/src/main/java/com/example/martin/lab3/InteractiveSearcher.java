package com.example.martin.lab3;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Martin on 2016-05-11.
 */

//InteractiveSearcher är en klass. Klassen består av en sökfält som gör det möjligt att söka efter namn
//där namnförslag dyker upp från en namnlista hämtat från "http://flask-afteach.rhcloud.com/getnames/".
//Varje sökning har ett eget id "searchIndex". Namnförslagen dyker med hänsyn till användarens input.
//Skriver användaren "Ma" i sökfältet kommer namnförlag som "Martin" att dyka upp dvs det kommer vara
//matchande till användarens sökningar.
//Asynctask används för att hämta namnen och det är för att inte hämta namnen genom UI-tråden.
//Servern kommer att retunera ett JSON-objekt som konverteras och sedan läggs dessa i en Hashmap.
//NameAdapter och NameList är två klasser som InteractiveSeacher använder sig av.


public class InteractiveSearcher extends EditText{  //Interactivesearcher är en komponent som ärver från EditText

    private Context context;
    private HashMap<Integer, ArrayList<String>> nameHashMap;
    private ListPopupWindow listPopupWindow;
    private boolean isClicked;
    private NameAdapter nameAdapter;
    public final static String noMatchingName = "No matching names found"; // Används i klassen NameList.
    private HttpURLConnection urlConnection;
    private int searchIndex;

    public InteractiveSearcher(Context theContext){
        super(theContext);
        context = theContext;
        init(); //ta från funktionen init()
    }


    private void init(){

        searchIndex = 0; //första indexet. Varje sökning ska få ett eget id som jag har döpt till searchIndex.

        nameHashMap = new HashMap<Integer, ArrayList<String>>(); //Hashmap används för att förvarar namnen som läses in

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); //Textfältet täcker hela skärmen

        //skapa popup-lista för att visa namn från klassen NameAdapter som är en adapter
        listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAnchorView(this); //placera pupuplistan under edittext

        //Lyssnare till popuplitan.
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isClicked = true; //sätter isClicked som true när man trycker på popuplistan
                setText(nameAdapter.getItem(position).toString()); // Om ett namn klickats så sätt namnet i fältet för text.
            }
        });

        //Lyssnare för om texten ändras. Alla hålls som standard förrutom afterTextChanged.
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0 || isClicked == true) { //om textfältet inte består av en nån bokstav/siffra dvs när den är tom eller om ett namn har blivit klickat på
                    listPopupWindow.dismiss(); //ta bort (visa inte) popuplistan.
                    isClicked = false;
                } else {
                    //Skapa en ny AsyncTask som körs i bakgrunden.
                    new NetworkAsyncTask().execute(s.toString());
                }
            }
        });
    }

    //Vi skapar en innerklass som ärver från AsyncTask och ska hämta namnförslag från serven.
    public class NetworkAsyncTask extends AsyncTask<String, Void, String> {

         //Startas direkt när AsyncTask kallas.

        protected String doInBackground(String... theSearchString){
            String returnedText = "";

            //Försöka få data från hemsidan.
            try{
                URL url = new URL("http://flask-afteach.rhcloud.com/getnames/" + searchIndex++ + "/" + theSearchString[0]);
                System.out.println("searchIndex = " + searchIndex);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                returnedText = retrieveNamesFromStream(inputStream);
                urlConnection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return returnedText;
        }


        //tar namnen från serven för att göra de till String.
        private String retrieveNamesFromStream(InputStream inputStream){
            StringBuilder stringBuilder = new StringBuilder();
            //Konverterar bytes till chars.
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int sizeOfBuffer = 1000;

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader, sizeOfBuffer);

            try {
                //Lägger till varje rad som inte är null till stringBuilder.
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    stringBuilder.append(line);
                }
                inputStream.close();

            } catch (Exception e) {
                System.out.println("Error reading the lines in the buffer." );
                e.printStackTrace();
            }
            finally {
                //Stäng readers.
                try{
                    if(bufferedReader == null) bufferedReader.close();
                    if(inputStreamReader == null) inputStreamReader.close();
                }catch (Exception e){
                    System.out.println("Error closing the readers.");
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString(); //en string med alla namnen från servern.
        }

        private void addNamesToHashmap(JSONArray resultsArray) {
            try {
                ArrayList<String> namesList = new ArrayList<String>();

                //Ett fall där det inte finns några matchande namn.
                if (resultsArray.length() == 0) {
                    namesList.add(noMatchingName);
                    nameHashMap.put(searchIndex, namesList);
                }
                else {
                    //Lägg alla namn från JSON-array till hashmap och listan med namn.
                    for (int idx = 0; idx < resultsArray.length(); idx++) {
                        namesList.add(resultsArray.get(idx).toString());
                    }
                    nameHashMap.put(searchIndex, namesList);
                }
            }
            catch(Exception e) {
                System.out.println("Error when adding names to the hashmap.");
                e.printStackTrace();
            }
        }

        //När all data har hämtats från servern så kallas OnPostExecute.
        //Här läggs alla namnen i en hashmap för att visa popuplistan med alla namförslagen.
        protected void onPostExecute(String theNames) {
            try{
                JSONObject jsonObject = new JSONObject(theNames);

                //Ta JSON-array som returnerades från servern som heter "result".
                JSONArray resultsArray = jsonObject.getJSONArray("result");

                //Lägga dit namnen till en hashmap.
                addNamesToHashmap(resultsArray);

            }catch (Exception e){
                System.out.println("Error in onPostExecute.");
                e.printStackTrace();
            }

            //Adaptern initieras till alla namn.
            nameAdapter = new NameAdapter(context, nameHashMap.get(searchIndex));

            //Höjden och bredden på popup-fönstret ska alltid wrap_content.
            listPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            listPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            //Visa adaptern.
            listPopupWindow.setAdapter(nameAdapter);
            listPopupWindow.show();
        }
    }
}
