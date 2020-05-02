package com.exampexample.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    /** A RequestQueue for the API. */
    private static RequestQueue queue;
    /** The variable for the button. */
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // need a button such that when it is clicked, API call is made through onClick.
        queue = Volley.newRequestQueue(MainActivity.this);
        call();

        // when button is clicked send user to the details of that pokemon they clicked
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetail();
            }
        });
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
                                    // a pokemon.
                                    JsonObject pokemon = results.get(i).getAsJsonObject();

                                    // To get the amount of buttons needed to be created
                                    int pokemonCount = pokemon.get("count").getAsInt();

                                    // to see what the pokemon name is.
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

    /** Method to transfer to the DetailsActivity. */
    public void openDetail() {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }
}
