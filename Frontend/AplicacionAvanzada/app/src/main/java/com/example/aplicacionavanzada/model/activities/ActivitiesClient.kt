package com.example.aplicacionavanzada.model.activities

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivitiesClient {

    @GET("activity")
    suspend fun getActivities(
        @Header("Authorization") bearerToken: String
    ): Response<List<ActivityResponse>>

    @GET("participation/user/{userId}")
    suspend fun getUserParticipation(
        @Header("Authorization") bearerToken: String,
        @Path("userId") userId: String
    ): Response<List<ParticipationResponse>>

    @POST("participation")
    suspend fun setUserParticipation(
        @Header("Authorization") bearerToken: String,
        @Body participationRequest: ParticipationRequest
    ): Response<ParticipationResponse>

    @DELETE("participation/{participationId}")
    suspend fun deleteUserParticipation(
        @Header("Authorization") bearerToken: String,
        @Path("participationId") participationId: String
    )
}