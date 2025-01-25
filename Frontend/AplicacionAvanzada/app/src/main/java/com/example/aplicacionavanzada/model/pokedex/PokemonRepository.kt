package com.example.aplicacionavanzada.model.pokedex

class PokemonRepository {
    private val pokemonClient = RetrofitInstance.pokemonClient

    suspend fun fetchPokemonList(): List<PokemonData> {
        return pokemonClient.getPokemonList()
    }
}