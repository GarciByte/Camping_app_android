package com.example.aplicacionavanzada.viewmodel.pokedex

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionavanzada.model.pokedex.PokemonData
import com.example.aplicacionavanzada.model.pokedex.PokemonRepository
import kotlinx.coroutines.launch

class ViewModelPokemon : ViewModel() {
    private val repository = PokemonRepository()

    private val _pokemonList = MutableLiveData<List<PokemonData>>()
    val pokemonList: LiveData<List<PokemonData>> get() = _pokemonList

    fun fetchPokemons() {
        viewModelScope.launch {
            try {
                val pokemons = repository.fetchPokemonList()
                _pokemonList.value = pokemons
            } catch (e: Exception) {
                Log.e("ViewModelPokemon", "Error obteniendo los Pokémon: ${e.message}")
            }
        }
    }
}