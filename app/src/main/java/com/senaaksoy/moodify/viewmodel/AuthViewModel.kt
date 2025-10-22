package com.senaaksoy.moodify.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.moodify.repository.AuthRepository
import com.senaaksoy.moodify.screens.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val currentUser get() = authRepository.currentUser

    //  UI Input State Variables
    var inputEmail by mutableStateOf("")
        private set
    var inputPassword by mutableStateOf("")
        private set
    var inputUsername by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var confirmPasswordVisibility by mutableStateOf(false)
    var showDialog by mutableStateOf(false)

    //  Update Input Fields
    fun updateInputPassword(password: String) {
        inputPassword = password
    }
    fun updateUsername(username: String) {
        inputUsername = username
    }
    fun updateInputEmail(email: String) {
        inputEmail = email
    }
    fun updateNewPassword(password: String) { newPassword = password }
    fun updateConfirmPassword(password: String) { confirmPassword = password }

    //AuthState
    private val _authState = MutableStateFlow(AuthState.EMPTY)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun passwordsMatch(): Boolean = newPassword == confirmPassword && newPassword.length >= 6
    fun resetPassword(oobCode: String) {
        viewModelScope.launch {
            try {
                authRepository.verifyPasswordResetCode(oobCode)
                authRepository.confirmPasswordReset(oobCode, newPassword)
                _authState.value = AuthState.SUCCESS
            } catch (e: Exception) {
                _authState.value = AuthState.FAILURE
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _authState.value = try {
                authRepository.registerUser(inputEmail, inputPassword, inputUsername)


                resetInputs()
                AuthState.SUCCESS
            } catch (e: Exception) {
                when {
                    e.message?.contains("email") == true ->
                        AuthState.USER_ALREADY_EXISTS
                    else -> AuthState.FAILURE
                }
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _authState.value = try {
                // Firebase ile giriş yap
                val result = authRepository.signIn(inputEmail, inputPassword)
                val user = result.user

                // Kullanıcı var mı ve e-posta doğrulandı mı?
                if (user != null && user.isEmailVerified) {
                    AuthState.SUCCESS
                } else {
                    // Eğer kullanıcı yoksa veya e-posta doğrulanmadıysa çıkış yap
                    authRepository.logOut()
                    AuthState.EMAIL_NOT_VERIFIED
                }
            } catch (e: Exception) {
                // Hataları yönet
                when (e) {
                    is com.google.firebase.auth.FirebaseAuthInvalidUserException -> AuthState.INVALID_CREDENTIALS
                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> AuthState.INVALID_EMAIL_OR_PASSWORD
                    else -> AuthState.FAILURE
                }
            }
        }
    }

    fun logOut() {
        authRepository.logOut()
        _authState.value = AuthState.EMPTY
    }

    fun resetAuthState() {
        _authState.value = AuthState.EMPTY
    }

    fun sendPasswordResetEmail() {
        viewModelScope.launch {
            try {
                authRepository.sendPasswordResetEmail(inputEmail)
                _authState.value = AuthState.PASSWORD_RESET_EMAIL_SENT
            } catch (e: Exception) {
                _authState.value = AuthState.FAILURE
            }
        }
    }












    // Validation Functions
    fun isvalidEmail() = Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()
    fun isValidUsername() : Boolean {
        val regex = "^(?![_.])[A-Za-z0-9._]{3,15}(?<![_.])$".toRegex()
        return inputUsername.matches(regex)
    }
    fun isvalidPassword() = if (inputPassword.isNotBlank()) {
        inputPassword.length == 6
    } else {
        false
    }
    fun isvalid() =
        isvalidPassword() && isvalidEmail() && isValidUsername()

    fun isValidSignIn(): Boolean {
        return inputEmail.isNotBlank() && inputPassword.isNotBlank()
    }

    //  Supporting Text States
    fun emailSupportText() = !isvalidEmail() && inputEmail.isNotBlank()
    fun passwordSupportText() = !isvalidPassword() && inputPassword.isNotBlank()
    fun usernameSupportText() = !isValidUsername() && inputUsername.isNotBlank()


    //  UI Helper Variables (password visibility)
    var passwordVisibility by mutableStateOf(false)

    fun resetInputs() {
        inputEmail = ""
        inputPassword = ""
        inputUsername = ""
    }

}