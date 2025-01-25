package com.example.aplicacionavanzada.model.pokedex

data class NextEvolution(
    val num: String,
    val name: String
)

data class PokemonData(
    val id: Int,
    val num: String,
    val name: String,
    val img: String,
    val type: List<String>,
    val height: String,
    val weight: String,
    val candy: String,
    val candy_count: Int,
    val egg: String,
    val spawn_chance: Float,
    val avg_spawns: Float,
    val spawn_time: String,
    val multipliers: List<Float>,
    val weaknesses: List<String>,
    val next_evolution: List<NextEvolution>,
    val prev_evolution: List<NextEvolution>
)