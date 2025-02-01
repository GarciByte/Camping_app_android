package com.example.aplicacionavanzada.viewmodel.authentication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionavanzada.model.authentication.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

@SuppressLint("StaticFieldLeak")
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val authRepository = AuthRepository()

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStoreAuth")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken

    private val _refreshToken = MutableStateFlow("")
    val refreshToken: StateFlow<String> = _refreshToken

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail

    init {
        getData()
    }

    // Cargar datos desde DataStore
    private fun getData() {
        viewModelScope.launch {
            val preferences = context.dataStore.data.first()
            _accessToken.value = preferences[ACCESS_TOKEN] ?: ""
            _refreshToken.value = preferences[REFRESH_TOKEN] ?: ""
            _userId.value = preferences[USER_ID] ?: ""
            _userEmail.value = preferences[USER_EMAIL] ?: ""

            if (_accessToken.value.isNotEmpty() && _refreshToken.value.isNotEmpty() && _userId.value != null && _userEmail.value != null) {
                _authState.value = AuthState.Authenticated

                Log.d("AuthViewModel", "Cargando datos desde DataStore:")
                Log.d("AuthViewModel", "Token de acceso: " + _accessToken.value)
                Log.d("AuthViewModel", "Token de refresco: " + _refreshToken.value)
                Log.d("AuthViewModel", "ID del usuario: " + _userId.value!!)
                Log.d("AuthViewModel", "Email del usuario: " + _userEmail.value!!)
                Log.d("AuthViewModel", "Fin de la carga de datos.")

            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    // Guardar en DataStore
    private suspend fun saveData(
        accessToken: String,
        refreshToken: String,
        userId: String,
        userEmail: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = userEmail
        }
        Log.d("AuthViewModel", "Datos guardados en DataStore.")
    }

    // Validar email y contraseña
    private fun validateCredentials(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            _authState.value = AuthState.Error("empty_email")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState.Error("invalid_email")
            return false
        }

        if (password.isEmpty()) {
            _authState.value = AuthState.Error("empty_password")
            return false
        }

        if (password.length < 6) {
            _authState.value = AuthState.Error("invalid_password")
            return false
        }

        return true
    }

    // Iniciar sesión
    fun login(email: String, password: String) {
        if (validateCredentials(email, password)) {
            _authState.value = AuthState.Loading
            viewModelScope.launch {
                val loginResponse = authRepository.login(email, password)

                if (loginResponse.accessToken.isNotEmpty() && loginResponse.refreshToken.isNotEmpty()) {
                    _accessToken.value = loginResponse.accessToken
                    _refreshToken.value = loginResponse.refreshToken

                    Log.d("AuthViewModel", "Inicio de sesión exitoso.")

                    // Obtener datos del usuario y guardarlos
                    getUserDataAndSave(email)
                    _authState.value = AuthState.Authenticated

                } else {
                    _authState.value = AuthState.Error("login_error")
                }
            }
        }
    }

    // Registrar usuario
    fun signUp(email: String, password: String) {
        if (validateCredentials(email, password)) {
            _authState.value = AuthState.Loading
            viewModelScope.launch {
                val signUpResponse = authRepository.singup(email, password)

                if (signUpResponse.email.isNotEmpty()) {
                    Log.d("AuthViewModel", "Registro exitoso.")
                    val loginResponse = authRepository.login(email, password)

                    if (loginResponse.accessToken.isNotEmpty() && loginResponse.refreshToken.isNotEmpty()) {
                        _accessToken.value = loginResponse.accessToken
                        _refreshToken.value = loginResponse.refreshToken

                        Log.d("AuthViewModel", "Inicio de sesión exitoso.")

                        // Obtener datos del usuario y guardarlos
                        getUserDataAndSave(email)
                        _authState.value = AuthState.Authenticated

                    } else {
                        _authState.value = AuthState.Error("login_error")
                    }

                } else {
                    _authState.value = AuthState.Error("signup_error")
                }
            }
        }
    }

    // Cerrar sesión
    fun signOut() {
        viewModelScope.launch {
            context.dataStore.edit { it.clear() }
            _authState.value = AuthState.Unauthenticated
            _accessToken.value = ""
            _refreshToken.value = ""
            _userId.value = null
            _userEmail.value = null
        }
    }

    // Obtener datos del usuario y guardarlos
    private fun getUserDataAndSave(email: String) {
        viewModelScope.launch {
            val accessToken = _accessToken.value
            if (accessToken.isNotEmpty()) {
                val response = authRepository.getUser(email, accessToken)

                if (response.id.isNotEmpty() && response.email.isNotEmpty()) {
                    _userId.value = response.id
                    _userEmail.value = response.email

                    // Guardar datos del usuario en DataStore
                    saveData(
                        _accessToken.value,
                        _refreshToken.value,
                        _userId.value ?: "",
                        _userEmail.value ?: ""
                    )

                    Log.d("AuthViewModel", "Obteniendo datos desde la API:")
                    Log.d("AuthViewModel", "Token de acceso: " + _accessToken.value)
                    Log.d("AuthViewModel", "Token de refresco: " + _refreshToken.value)
                    Log.d("AuthViewModel", "ID del usuario: " + _userId.value!!)
                    Log.d("AuthViewModel", "Email del usuario: " + _userEmail.value!!)

                } else {
                    _authState.value = AuthState.Error("server_error")
                }
            }
        }
    }

    // Refrescar token de acceso
    fun refreshAndSaveToken() {
        viewModelScope.launch {
            val refreshToken = _refreshToken.value
            if (refreshToken.isNotEmpty()) {
                val response = authRepository.refreshToken(refreshToken)

                if (response.token.isNotEmpty()) {
                    _accessToken.value = response.token

                    Log.d("AuthViewModel", "Se ha refrescado el token.")

                    // Guardar datos del usuario en DataStore
                    saveData(
                        _accessToken.value,
                        _refreshToken.value,
                        _userId.value ?: "",
                        _userEmail.value ?: ""
                    )

                    _authState.value = AuthState.Authenticated

                } else {
                    _authState.value = AuthState.Error("token_refresh_error")
                }
            }
        }
    }
}

sealed class AuthState {
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val messageKey: String) : AuthState()
}