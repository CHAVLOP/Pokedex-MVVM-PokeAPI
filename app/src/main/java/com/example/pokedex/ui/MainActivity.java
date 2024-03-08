package com.example.pokedex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pokedex.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Button btnPokemonPorGPS = findViewById(R.id.btnPokemonPorGPS);
        Button btnListaDePokemon = findViewById(R.id.btnListaDePokemon);
        Button btnBuscarPokemonAlAzar = findViewById(R.id.btnBuscarPokemonAlAzar);
        btnPokemonPorGPS.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GpsActivity.class);
            startActivity(intent);
        });
        btnListaDePokemon.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, PokemonList.class);
            startActivity(intent);
        });
        btnBuscarPokemonAlAzar.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }
}

