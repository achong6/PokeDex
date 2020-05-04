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

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /** A RequestQueue for the API. */
    private static RequestQueue queue;
    /** The arrays to get data for the List view. */
    private ArrayList<String> PokemonNames = new ArrayList<String>();
    private ListView lv;

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
        //made REST limit 964 to access all Pokemon at once.
        String url = "https://pokeapi.co/api/v2/pokemon?limit=964";

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String JsonString = response.toString();
                                JsonObject object = JsonParser.parseString(JsonString).getAsJsonObject();
                                JsonArray results = object.get("results").getAsJsonArray();
                                for (int i = 0; i < results.size(); i++) {
                                    // a pokemon.
                                    JsonObject pokemon = results.get(i).getAsJsonObject();


                                    //add pokemon names to the Pokemon Name List.
                                    PokemonNames.add(i, pokemon.get("name").getAsString());
                                    //PokemonIDs.add(i,pokemon.get("id").getAsInt());

                                    //sanity check: see if the PokemonList contains every name (it does when i ran it).
                                    //for (int k = 0; k < PokemonNames.size(); k++) {
                                    //    Log.d("pokemonName", PokemonNames.get(k));
                                    //}

                                    // now make another API call to access the selected pokemon's values.
                                    //************* TODO: Add height, etc. to lists (don't know what PokemonIDlist means...). The code should have gotten all the heights/weight/type but make sure....
                                    //**** USE LOGCAT (find it using "search" to see the printed values to console.)

                                    String pokeURL = pokemon.get("url").getAsString();
                                    JsonObjectRequest pokeRequest = new JsonObjectRequest
                                            (Request.Method.GET, pokeURL, null,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject pokeResponse) {
                                                            String JsonString = pokeResponse.toString();
                                                            final JsonObject pokeObject = JsonParser.parseString(JsonString).getAsJsonObject();

                                                            // height.
                                                            final int height = pokeObject.get("height").getAsInt();
                                                            //Log.d("height", String.valueOf(height));

                                                            // weight.
                                                            final int weight = pokeObject.get("weight").getAsInt();
                                                            //Log.d("weight", String.valueOf(weight));

                                                            //types (note there can be more than one).

                                                            JsonArray types = pokeObject.get("types").getAsJsonArray();
                                                            for (int j = 0; j < types.size(); j++) {
                                                                JsonObject type = types.get(j).getAsJsonObject().get("type").getAsJsonObject();
                                                                String typeName = type.get("name").getAsString();
                                                                //print type name to logcat.
                                                                //Log.d("type", typeName);
                                                            }
                                                            // creates a list view
                                                            lv = (ListView) findViewById(R.id.listView);
                                                            // adds the layout for each individual list and places it into the view
                                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, PokemonNames);
                                                            lv.setAdapter(arrayAdapter);
                                                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                                                                    intent.putExtra("name", PokemonNames.get(position));
                                                                    System.out.println(PokemonNames.get(position));
                                                                    intent.putExtra("weight", weight);
                                                                    intent.putExtra("height", height);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // TODO: Handle error
                                                    Log.e("specific Pokemon error", "no");
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
