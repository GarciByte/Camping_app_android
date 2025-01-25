package com.david.servidor.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("USERS")
data class User(
  @Id var id: String? = null,
  val email: String,
  val password: String,
  val role: Role
)

enum class Role {
  USER, ADMIN
}