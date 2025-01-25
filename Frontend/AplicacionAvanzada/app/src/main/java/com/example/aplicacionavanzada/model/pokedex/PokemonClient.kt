package com.example.aplicacionavanzada.model.pokedex

import retrofit2.http.GET

interface PokemonClient {
    @GET("/v3/3b299b17-b069-41e8-bf5c-e0ae4263ac89")
    suspend fun getPokemonList(): List<PokemonData>
}