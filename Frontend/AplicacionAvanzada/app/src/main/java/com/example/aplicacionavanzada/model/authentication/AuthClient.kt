package com.example.aplicacionavanzada.model.authentication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthClient {

    @POST("auth")
    suspend fun login(@Body authRequest: AuthRequest): Response<LoginResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body tokenRequest: TokenRequest): Response<TokenResponse>

    @POST("user")
    suspend fun singup(@Body authRequest: AuthRequest): Response<SignUpResponse>

    @GET("user/email/{email}")
    suspend fun getUser(
        @Path("email") email: String,
        @Header("Authorization") bearerToken: String
    ): Response<SignUpResponse>
}