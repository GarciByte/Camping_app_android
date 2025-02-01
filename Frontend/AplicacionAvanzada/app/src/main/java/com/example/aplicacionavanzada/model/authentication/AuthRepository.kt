package com.example.aplicacionavanzada.model.authentication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val authClient = AuthRetrofitInstance.authClient

    // Login
    suspend fun login(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            val response = authClient.login(AuthRequest(email, password))
            LoginResponse(
                response.body()?.accessToken ?: "",
                response.body()?.refreshToken ?: ""
            )
        }
    }

    // Refresh Token
    suspend fun refreshToken(token: String): TokenResponse {
        return withContext(Dispatchers.IO) {
            val response = authClient.refreshToken(TokenRequest(token))
            TokenResponse(response.body()?.token ?: "")
        }
    }

    // Singup
    suspend fun singup(email: String, password: String): SignUpResponse {
        return withContext(Dispatchers.IO) {
            val response = authClient.singup(AuthRequest(email, password))
            SignUpResponse(
                response.body()?.id ?: "",
                response.body()?.email ?: "",
                response.body()?.role ?: ""
            )
        }
    }

    // Buscar un usuario por su email
    suspend fun getUser(email: String, accessToken: String): SignUpResponse {
        val bearerToken = "Bearer $accessToken"
        return withContext(Dispatchers.IO) {
            val response = authClient.getUser(email, bearerToken)
            SignUpResponse(
                response.body()?.id ?: "",
                response.body()?.email ?: "",
                response.body()?.role ?: ""
            )
        }
    }
}
