package com.example.aplicacionavanzada.model.authentication

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)