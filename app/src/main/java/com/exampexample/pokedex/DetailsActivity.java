package com.exampexample.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/** Class for the user to see the stats of the pokemon they selected in MainActivity. */
public class DetailsActivity extends AppCompatActivity {
    /** The variable for the button. */
    private Button button;
    private TextView height;
    private TextView weight;
    private TextView pokemoName;
    private TextView pokemonType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // details of the text view in the activity
        pokemoName = findViewById(R.id.PokemonName);
        height = findViewById(R.id.PokemonHeight);
        weight = findViewById(R.id.PokemonWeight);
        pokemonType = findViewById(R.id.PokemonType1);

        // gets data from MainActivity
        Intent intent = getIntent();
        String currentName = intent.getStringExtra("name");
        pokemoName.setText(currentName);
        String currentType = intent.getStringExtra("type");
        pokemonType.setText("Type: " + currentType);
        int currentHeight = intent.getIntExtra("height", 0);
        height.setText("Height: " + currentHeight);
        int currentWeight = intent.getIntExtra("weight", 0);
        weight.setText("Weight: " + currentWeight + "lb");

        // Edits the font size of the text views
        pokemoName.setTextSize(30);
        pokemoName.setTextColor(Color.BLACK);
        pokemonType.setTextSize(30);
        pokemonType.setTextColor(Color.BLACK);
        height.setTextSize(25);
        height.setTextColor(Color.BLACK);
        weight.setTextSize(25);
        weight.setTextColor(Color.BLACK);

        // when clicked, send user back to the main screen.
        button = (Button) findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }

    /** Method to bring user back to MainActivity. */
    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
