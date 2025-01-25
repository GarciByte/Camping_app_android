package com.david.servidor.controller.auth

data class AuthenticationRequest(
  val email: String,
  val password: String,
)