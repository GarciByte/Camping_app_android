package com.david.servidor.controller.activity

data class ActivityRequest(
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val place: String
)