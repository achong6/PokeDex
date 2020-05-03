package com.exampexample.pokedex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /** A RequestQueue for the API. */
    private static RequestQueue queue;
    /** The variable for the button. */
    //private Button button;
    /** The arrays to get data for the List view. */
    private ArrayList<String> PokemonNames = new ArrayList<String>();
    private ArrayList<Integer> PokemonIDs = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creates a list view
        ListView lv = (ListView) findViewById(R.id.listview);
        addListContent();
        // need a button such that when it is clicked, API call is made through onClick.
        queue = Volley.newRequestQueue(MainActivity.this);
        call();
        // adds the layout for each individual list and places it into the view
        lv.setAdapter(new MyListAdapter(this, R.layout.list_item, PokemonNames));
    }

    // used to add the items to the corrosponding list
    private void addListContent() {
        for (int i = 0; i < count; i++) {
            PokemonNames.add();
            PokemonIDs.add();
        }
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

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewBuilder mainViewBuilder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent,false);
                ViewBuilder viewBuilder = new ViewBuilder();
                viewBuilder.pokemonImage = (ImageView) convertView.findViewById(R.id.PokemonImageMain);
                viewBuilder.pokemonID = (TextView) convertView.findViewById(R.id.PokemonIDMain);
                viewBuilder.button = (Button) convertView.findViewById(R.id.buttonMain);
                viewBuilder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDetail();
                    }
                });
                convertView.setTag(viewBuilder);
            } else {
                mainViewBuilder = (ViewBuilder) convertView.getTag();
                mainViewBuilder.pokemonID.setText(getItem(position));
            }
            return convertView;
        }
    }
    public class ViewBuilder {
        ImageView pokemonImage;
        TextView pokemonID;
        Button button;
    }
    /** Method to transfer to the DetailsActivity. */
    public void openDetail() {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }
}
