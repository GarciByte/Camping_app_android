package com.david.servidor.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class JwtProperties(
  val key: String? = null,
  val accessTokenExpiration: Long,
  val refreshTokenExpiration: Long,
)
