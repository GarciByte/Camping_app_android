package com.david.servidor.service

import com.david.servidor.model.User
import com.david.servidor.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
  private val userRepository: UserRepository
) {
  // Crear usuario nuevo
  fun createUser(user: User): User? = userRepository.save(user)

  // Obtener usuario por ID
  fun findById(id: String): User? = userRepository.findById(id)

  // Obtener usuario por email
  fun findByEmail(email: String): User? = userRepository.findByEmail(email)

  // Obtener todos los usuarios
  fun findAll(): List<User> = userRepository.findAll()

  // Borrar usuario
  fun deleteById(id: String): Boolean = userRepository.deleteById(id)
}

