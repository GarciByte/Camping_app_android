package com.david.servidor.repository

import com.david.servidor.model.Role
import com.david.servidor.model.User
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
class UserRepository(
  private val db: JdbcTemplate,
  private val encoder: PasswordEncoder
) {

  // Registrar usuario nuevo
  fun save(user: User): User? {
    val existingUser = findByEmail(user.email)
    if (existingUser == null) {
      val generatedId = user.id ?: UUID.randomUUID().toString()
      db.update(
        "INSERT INTO USERS (id, email, password, role) VALUES (?, ?, ?, ?)",
        generatedId, user.email, encoder.encode(user.password), user.role.name
      )
      return user.copy(id = generatedId)
    } else {
      return null
    }
  }

  // Borrar usuario
  fun deleteById(id: String): Boolean {
    val existingUser = findById(id)
    if (existingUser == null) {
      return false
    } else {
      db.update("DELETE FROM USERS WHERE id = ?", id)
      return true
    }
  }

  // Obtener usuario por ID
  fun findById(id: String): User? {
    return try {
      db.queryForObject(
        "SELECT * FROM USERS WHERE id = ?",
        { response, _ -> userMap(response) },
        id
      )
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  // Obtener usuario por email
  fun findByEmail(email: String): User? {
    return try {
      db.queryForObject(
        "SELECT * FROM USERS WHERE email = ?",
        { response, _ -> userMap(response) },
        email
      )
    } catch (e: EmptyResultDataAccessException) {
      null
    }
  }

  // Obtener todos los usuarios
  fun findAll(): List<User> {
    return db.query("SELECT * FROM USERS") { response, _ -> userMap(response) }
  }

  private fun userMap(rs: ResultSet): User {
    return User(
      id = rs.getString("id"),
      email = rs.getString("email"),
      password = rs.getString("password"),
      role = Role.valueOf(rs.getString("role"))
    )
  }

}
