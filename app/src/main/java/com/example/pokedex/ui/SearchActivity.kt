package com.example.pokedex.ui// com.example.pokedex.ui.SearchActivity.kt
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.R
import com.example.pokedex.api.Pokemon
import com.example.pokedex.api.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView

class SearchActivity : AppCompatActivity() {

    private lateinit var searchPokemonButton: Button
    private lateinit var pokemonImageView: ImageView
    private lateinit var pokemonNameTextView: TextView
    private lateinit var pokemonHeightTextView: TextView
    private lateinit var pokemonWeightTextView: TextView
    private lateinit var pokemonTypeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        searchPokemonButton = findViewById(R.id.searchPokemonButton)
        pokemonImageView = findViewById(R.id.pokemonImageView)
        pokemonNameTextView = findViewById(R.id.pokemonNameTextView)
        pokemonHeightTextView = findViewById(R.id.pokemonHeightTextView)
        pokemonWeightTextView = findViewById(R.id.pokemonWeightTextView)
        pokemonTypeTextView = findViewById(R.id.pokemonTypeTextView)

        searchPokemonButton.setOnClickListener {
            getRandomPokemon()
        }
    }

    private fun getRandomPokemon() {
        val randomPokemonId = (1..151).random()
        val pokeApiService = RetrofitClient.apiService
        val call = pokeApiService.getPokemonInfo(randomPokemonId)

        call.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful) {
                    val pokemonResponse = response.body()
                    pokemonResponse?.let { showPokemon(it) }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun showPokemon(pokemonResponse: Pokemon) {
        val pokemonName = pokemonResponse.name
        val imageUrl = pokemonResponse.sprites.frontDefault

        // Show the name of the Pokemon using a Toast message
        Toast.makeText(this, "¡Has encontrado a $pokemonName!", Toast.LENGTH_SHORT).show()

        // Load the image into the ImageView using Picasso
        imageUrl?.let {
            Picasso.get().load(it).into(pokemonImageView)
        }

        // Set Pokemon name
        pokemonNameTextView.text = pokemonName

        // Set Pokemon height
        pokemonHeightTextView.text = "Height: ${pokemonResponse.height}"

        // Set Pokemon weight
        pokemonWeightTextView.text = "Weight: ${pokemonResponse.weight}"

        // Set Pokemon type
        val types = pokemonResponse.types.joinToString { it.type.name }
        pokemonTypeTextView.text = "Type(s): $types"
    }
}
