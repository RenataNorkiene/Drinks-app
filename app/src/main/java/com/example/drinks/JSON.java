package com.example.drinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    } //readAll end

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }//readJsonFromUrl end
    // trys metodai svarbus norint issitraukti norimus duomenis is JSON
    public static ArrayList<Drinks> getList(JSONArray jsonArray) throws JSONException{
        ArrayList<Drinks> drinksList = new ArrayList<Drinks>(); //created list with all drinks
        //extract data from JSON and save it in Drinks Objects List(drinksList)
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Drinks drinks = new Drinks( //constructor for one object
                    // public Drinks(String id, String name, String tags, String category, String glass)
                    jsonObject.getString("idDrink"),
                    jsonObject.getString("strDrink"),
                    jsonObject.getString("strTags"),
                    jsonObject.getString("strCategory"),
                    jsonObject.getString("strGlass")
            );
            drinksList.add(drinks); //every objects of JSON goes true cycle and extracts data
        }
        return drinksList;
    }
    public static JSONArray getJSONArray(JSONObject jsonObject) throws JSONException{ //from array extract list

        JSONArray jsonArray = jsonObject.getJSONArray("drinks");
        return jsonArray; //sita jsonArray paims aukstesnis metodas getList ir grazins mums sarasa
    }

    public static ArrayList<Drinks> getDrinksListByName(ArrayList<Drinks> drinksArrayList, String coctailName){
        ArrayList<Drinks> drinksListByName = new ArrayList<Drinks>();
        for (Drinks drinks : drinksArrayList) { //kaireje bus sukuriamas tos klases objektas, per kurios sarasa iteruojame (desineje)
            if(drinks.getName().contains(coctailName)){ //contains method (string method)  search part of string/word (drink)
                drinksListByName.add(drinks);
            }
        }
        return drinksListByName;
    }
}//json end