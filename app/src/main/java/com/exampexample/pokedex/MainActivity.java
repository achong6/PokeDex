package com.exampexample.pokedex;

import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /**
     * a RequestQueue for the API.
     */
    private static RequestQueue queue;


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
        String url = "https://pokeapi.co/api/v2/pokemon";

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String JsonString = response.toString();
                                JsonObject object = JsonParser.parseString(JsonString).getAsJsonObject();
                                JsonArray results = object.get("results").getAsJsonArray();
                                for (int i = 0; i < results.size(); i++) {
                                    //a pokemon.
                                    JsonObject pokemon = results.get(i).getAsJsonObject();
                                    //to see what the pokemon name is.
                                    Log.d("pokemon", pokemon.get("name").getAsString());




                                    // now make another API call to access the selected pokemon's values.
                                    String pokeURL = pokemon.get("url").getAsString();
                                    JsonObjectRequest pokeRequest = new JsonObjectRequest
                                            (Request.Method.GET, pokeURL, null,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject pokeResponse) {
                                                            String JsonString = pokeResponse.toString();
                                                            JsonObject pokeObject = JsonParser.parseString(JsonString).getAsJsonObject();
                                                            JsonArray abilities = pokeObject.get("abilities").getAsJsonArray();
                                                            for (int i = 0; i < abilities.size(); i++) {
                                                                JsonObject ability = abilities.get(i).getAsJsonObject();
                                                                //print ability name to logcat.
                                                                Log.d("ability", ability.get("name").getAsString());
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // TODO: Handle error
                                                    Log.e("specific Pokemon error", "no");
                                                }
                                            });

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
