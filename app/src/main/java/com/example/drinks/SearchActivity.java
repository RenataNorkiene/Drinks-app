package com.example.drinks;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public static final String DRINKS_API = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita";
    private ArrayList<Drinks> drinksList = new ArrayList<Drinks>(); //sukuriamas saras

    private RecyclerView recyclerView; //korteliu vaizdas
    private Adapter adapter; //tarpininkas tarp searchActivity ir xml, apjungia dvi skirtingas klases

    private SearchView searchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //new thread(gija) starts (to read JSON from API)
        AsyncFetch asyncFetch = new AsyncFetch();
        asyncFetch.execute();//must be called to start onPressExecute, doInBackground
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //kad butu galimybe spausti it ieskoti reikia perrasyti metoda

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search, menu);
        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search); //kad butu galima irasyti i menu juosta ko ieskome

        //kreipiasi i bibliotekas ir vykdo pagal salygas:
        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            searchView.setIconified(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //atsiranda simboliu paieskos
        return super.onOptionsItemSelected(item);
    }
    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function

    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query
        //super.onNewIntent(intent);
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY); //issitraukiam ka vartotojas ivede

            if (searchView != null) {
                searchView.clearFocus();

            }

            //is visu gerimu saraso sukuriamas sarasas pagal ieskoma gerima(query)
            ArrayList<Drinks> drinksListByName = JSON.getDrinksListByName(drinksList, query);

            if (drinksListByName.size() == 0) {
                Toast.makeText(this, getResources().getString(R.string.search_no_results) + query, Toast.LENGTH_SHORT).show();
            }
            // duomenu perdavimas Adapteriui ir Recycleview sukurimas
            recyclerView = (RecyclerView) findViewById(R.id.drinks_list);
            adapter = new com.example.drinks.Adapter(SearchActivity.this, drinksListByName);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        }
    }

    private class AsyncFetch extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);

        @Override
        protected void onPreExecute() {
            //method implemented before doInBackground, user waits till gets info from API
            super.onPreExecute();
            progressDialog.setMessage(getResources().getString(R.string.search_loading_data));
            progressDialog.setCancelable(false); //user has to wait, can't to cancel
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            //will be executed while user waits (see progressDialog), gets JSON from API
            try {
                JSONObject jsonObject = JSON.readJsonFromUrl(DRINKS_API); //sioje vietoje perduosime URL
                return jsonObject;
            } catch (IOException e) { //input/output exceptions
                Toast.makeText(
                        SearchActivity.this,
                        getResources().getString(R.string.search_error_reading_data) + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            } catch (JSONException e) {
                Toast.makeText(
                        SearchActivity.this,
                        getResources().getString(R.string.search_error_reading_data) + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            } //catch ends
            return null;
        }//doIn Background ends

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();

            JSONArray jsonArray = null;
            try {
                jsonArray = com.example.drinks.JSON.getJSONArray(jsonObject);
                drinksList = com.example.drinks.JSON.getList(jsonArray);


            } catch (JSONException e) {
                Toast.makeText(SearchActivity.this,
                        getResources().getString(R.string.search_error_reading_data) + e.getMessage(), Toast.LENGTH_LONG
                ).show();
            } //catch ends

    } //onPostExecute ends
} //AsyncFetch class ends
//jeigu reiks dar kokiu metodu, rasysime cia
} //SearchActivity class ends

