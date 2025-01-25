package com.david.servidor.controller.user

import com.david.servidor.model.Role
import com.david.servidor.model.User
import com.david.servidor.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
  private val userService: UserService
) {

  // Crear un nuevo usuario
  @PostMapping
  fun create(@RequestBody userRequest: UserRequest): UserResponse =
    userService.createUser(userRequest.toModel())
      ?.toResponse()
      ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create user.")

  // Obtener todos los usuarios
  @GetMapping
  fun listAll(): List<UserResponse> =
    userService.findAll()
      .map { it.toResponse() }

  // Buscar un usuario por su ID
  @GetMapping("/{id}")
  fun findById(@PathVariable id: String, authentication: Authentication): UserResponse {
    val user = userService.findById(id)
      ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")

    val isAdmin = authentication.authorities.any { it.authority == "ROLE_ADMIN" }
    val isOwner = user.email == authentication.name

    if (!isOwner && !isAdmin) {
      throw ResponseStatusException(HttpStatus.FORBIDDEN, "Restricted access.")
    }

    return user.toResponse()
  }

  // Buscar un usuario por su email
  @GetMapping("/email/{email}")
  fun findByEmail(@PathVariable email: String, authentication: Authentication): UserResponse {
    val user = userService.findByEmail(email)
      ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")

    val isAdmin = authentication.authorities.any { it.authority == "ROLE_ADMIN" }
    val isOwner = user.email == authentication.name

    if (!isOwner && !isAdmin) {
      throw ResponseStatusException(HttpStatus.FORBIDDEN, "Restricted access.")
    }

    return user.toResponse()
  }

  // Eliminar un usuario por su ID
  @DeleteMapping("/{id}")
  fun deleteById(@PathVariable id: String): ResponseEntity<Boolean> {
    val success = userService.deleteById(id)

    return if (success)
      ResponseEntity.noContent()
        .build()
    else
      throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
  }

  private fun User.toResponse(): UserResponse =
    UserResponse(
      id = this.id.toString(),
      email = this.email,
      role = this.role.toString()
    )

  private fun UserRequest.toModel(): User =
    User(
      id = UUID.randomUUID().toString(),
      email = this.email,
      password = this.password,
      role = Role.USER
    )
}