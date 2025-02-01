package com.example.aplicacionavanzada.view.dialogs

import com.example.aplicacionavanzada.R

object ToastMessage {
    fun getStringResourceId(key: String): Int = when (key) {
        "empty_email" -> R.string.empty_email
        "invalid_email" -> R.string.invalid_email
        "empty_password" -> R.string.empty_password
        "invalid_password" -> R.string.invalid_password
        "login_error" -> R.string.login_error
        "signup_error" -> R.string.signup_error
        "server_error" -> R.string.server_error
        "token_refresh_error" -> R.string.token_refresh_error
        else -> R.string.generic_error
    }
}