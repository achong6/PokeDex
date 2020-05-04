package com.exampexample.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** Class for the user to see the stats of the pokemon they selected in MainActivity. */
public class DetailsActivity extends AppCompatActivity {
    /** The variable for the button. */
    private Button button;
    private TextView pokemonID;
    private TextView height;
    private TextView weight;
    private TextView pokemoName;
    private TextView pokemonType1;
    private TextView pokemonType2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // details of the text view in the activity
        pokemoName = findViewById(R.id.PokemonName);
        height = findViewById(R.id.PokemonHeight);
        weight = findViewById(R.id.PokemonWeight);
        pokemonID = findViewById(R.id.PokemonID);
        pokemonType1 = findViewById(R.id.PokemonType1);
        pokemonType2 = findViewById(R.id.PokemonType2);

        Intent intent = getIntent();
        String currentName = intent.getStringExtra("name");
        pokemoName.setText(currentName);

        int currentHeight = intent.getIntExtra("height", 0);
        int currentWeight = intent.getIntExtra("weight", 0);
        height.setText("" + currentHeight);
        weight.setText("" + currentWeight + "lb");

        // when clicked, send user back to the main screen.
        button = (Button) findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }

    /** Method to bring useer back to MainActivity. */
    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
