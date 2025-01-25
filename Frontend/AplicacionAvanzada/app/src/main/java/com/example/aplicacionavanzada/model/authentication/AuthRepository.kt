package com.example.aplicacionavanzada.model.authentication

import retrofit2.Response

class AuthRepository {
    private val authClient = AuthRetrofitInstance.authClient

    // Login
    suspend fun login(authRequest: AuthRequest): Response<LoginResponse> {
        return authClient.login(authRequest)
    }

    // Refresh Token
    suspend fun refreshToken(tokenRequest: TokenRequest): Response<TokenResponse> {
        return authClient.refreshToken(tokenRequest)
    }

    // Signup
    suspend fun signup(authRequest: AuthRequest): Response<SignUpResponse> {
        return authClient.singup(authRequest)
    }

    // Buscar un usuario por su email
    suspend fun getUser(email: String): Response<SignUpResponse> {
        return authClient.getUser(email)
    }
}
