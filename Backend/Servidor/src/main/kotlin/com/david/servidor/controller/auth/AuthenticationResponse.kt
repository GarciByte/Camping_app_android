package com.david.servidor.controller.auth

data class AuthenticationResponse(
  val accessToken: String,
  val refreshToken: String,
)