package com.david.servidor.controller.participation

data class ParticipationRequest(
    val userId: String,
    val activityId: Long
)