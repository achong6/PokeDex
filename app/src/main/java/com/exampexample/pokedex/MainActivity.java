package com.exampexample.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /** A RequestQueue for the API. */
    private static RequestQueue queue;

    ///** The arrays to get data for the List view. */
    private ArrayList<String> PokemonNames = new ArrayList<String>();
    private ListView lv;

    /** A Map for the Pokemon name and PokeObject */
    private static Map<String, JsonObject>  pokeMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // need a button such that when it is clicked, API call is made through onClick.
        queue = Volley.newRequestQueue(MainActivity.this);
        call();
    }

    // syntax from https://www.geeksforgeeks.org/volley-library-in-android/.
    // make API call to PokeApi.

    public void call() {
        //can change REST limit appropriately .
        String url = "https://pokeapi.co/api/v2/pokemon";

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                pokeMap = new HashMap<>();
                                String JsonString = response.toString();
                                JsonObject object = JsonParser.parseString(JsonString).getAsJsonObject();
                                JsonArray results = object.get("results").getAsJsonArray();
                                for (int i = 0; i < results.size(); i++) {
                                    // a pokemon.
                                    JsonObject pokemon = results.get(i).getAsJsonObject();
                                    final String name = pokemon.get("name").getAsString();



                                    //add pokemon names to the Pokemon Name List.
                                    PokemonNames.add(i, pokemon.get("name").getAsString());

                                    // now make another API call to access the selected pokemon's values.
                                    String pokeURL = pokemon.get("url").getAsString();
                                    JsonObjectRequest pokeRequest = new JsonObjectRequest
                                            (Request.Method.GET, pokeURL, null,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject pokeResponse) {
                                                            String JsonString = pokeResponse.toString();
                                                            final JsonObject pokeObject = JsonParser.parseString(JsonString).getAsJsonObject();

                                                            pokeMap.put(pokeObject.get("id").getAsString(), pokeObject);

                                                            // creates a list view
                                                            lv = (ListView) findViewById(R.id.listView);
                                                            // adds the layout for each individual list and places it into the view
                                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, PokemonNames);
                                                            lv.setAdapter(arrayAdapter);
                                                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    JsonObject object = pokeMap.get(String.valueOf(id + 1));
                                                                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                                                                    intent.putExtra("name", PokemonNames.get(position));
                                                                    intent.putExtra("weight", object.get("weight").getAsInt());
                                                                    intent.putExtra("height", object.get("height").getAsInt());

                                                                    //get types.

                                                                    JsonArray types = object.get("types").getAsJsonArray();
                                                                    String stringType = "";

                                                                    for (int j = 0; j < types.size(); j++) {
                                                                        JsonObject type = types.get(j).getAsJsonObject().get("type").getAsJsonObject();
                                                                        String typeName = type.get("name").getAsString();
                                                                        stringType = stringType + " " + typeName;
                                                                    }

                                                                    intent.putExtra("type", stringType);

                                                                    startActivity(intent);
                                                                }
                                                            });
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // TODO: Handle error
                                                    Log.e("specific Pokemon error", "error");
                                                }
                                            });

                                    // TODO: 2020-05-03 : implement try/catch blocks if have time. 

                                    queue.add(pokeRequest);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("some error", ":(");
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(request);
    }
}
