package com.david.servidor.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("ACTIVITIES")
data class Activity(
    @Id var id: Long? = null,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val place: String
)