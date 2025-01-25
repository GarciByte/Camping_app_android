package com.david.servidor.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("PARTICIPATIONS")
data class Participation(
    @Id var id: String? = null,
    val userId: String,
    val activityId: Long
)