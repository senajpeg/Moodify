package com.senaaksoy.moodify.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.senaaksoy.moodify.repository.AuthRepository
import com.senaaksoy.moodify.screens.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {
    val currentUser get() = authRepository.currentUser

    //GİRİŞ / KAYIT FORM ALANLARI
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

    //PROFİL FOTOĞRAFI YÖNETİMİ
    private val _selectedImageUrl = MutableStateFlow<String?>(null)
    val selectedImageUrl: StateFlow<String?> = _selectedImageUrl.asStateFlow()

    private val _isUploadingImage = MutableStateFlow(false)
    val isUploadingImage: StateFlow<Boolean> = _isUploadingImage.asStateFlow()

    init {
        loadProfileImage()
    }

    private fun loadProfileImage() {
        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val url = authRepository.getProfileImageUrl(uid)
                _selectedImageUrl.value = url
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        val uid = currentUser?.uid ?: return
        viewModelScope.launch {
            _isUploadingImage.value = true
            try {
                val url = authRepository.uploadProfileImage(uid, uri)
                authRepository.saveProfileImageUrl(uid, url)
                _selectedImageUrl.value = url
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isUploadingImage.value = false
            }
        }
    }

    //FORM GÜNCELLEME FONKSİYONLARI
    fun updateInputPassword(password: String) {
        inputPassword = password
    }

    fun updateUsername(username: String) {
        inputUsername = username
    }

    fun updateInputEmail(email: String) {
        inputEmail = email
    }

    fun updateNewPassword(password: String) {
        newPassword = password
    }

    fun updateConfirmPassword(password: String) {
        confirmPassword = password
    }

    //AUTH DURUM YÖNETİMİ
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

    fun getGoogleSignInIntent() = googleSignInClient.signInIntent

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _authState.value = try {
                googleSignInClient.signOut().await()
                val email = account.email ?: throw Exception("Email bulunamadı")

                val isRegistered = authRepository.isUserRegistered(email)
                if (isRegistered) {
                    authRepository.signInWithGoogle(account)
                    loadProfileImage()
                    AuthState.SUCCESS
                } else {
                    authRepository.signInWithGoogle(account)
                    authRepository.saveGoogleUser(account)
                    loadProfileImage()
                    AuthState.SUCCESS
                }
            } catch (e: Exception) {
                AuthState.FAILURE
            }
        }
    }

    fun startGoogleSignIn(onReady: () -> Unit) {
        viewModelScope.launch {
            try {
                googleSignInClient.signOut().await()
                onReady()
            } catch (e: Exception) {
                onReady()
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
                val result = authRepository.signIn(inputEmail, inputPassword)
                val user = result.user

                if (user != null && user.isEmailVerified) {
                    loadProfileImage()
                    AuthState.SUCCESS
                } else {
                    authRepository.logOut()
                    AuthState.EMAIL_NOT_VERIFIED
                }
            } catch (e: Exception) {
                when (e) {
                    is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
                        AuthState.INVALID_CREDENTIALS

                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                        AuthState.INVALID_EMAIL_OR_PASSWORD

                    else -> AuthState.FAILURE
                }
            }
        }
    }

    fun logOut() {
        authRepository.logOut()
        _authState.value = AuthState.EMPTY
        _selectedImageUrl.value = null
    }

    fun resetAuthState() { _authState.value = AuthState.EMPTY }

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

    //GEÇERLİLİK KONTROLLERİ
    fun isvalidEmail() = Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()

    fun isValidUsername(): Boolean {
        val regex = "^(?![_.])[A-Za-z0-9._]{3,15}(?<![_.])$".toRegex()
        return inputUsername.matches(regex)
    }

    fun isvalidPassword() = if (inputPassword.isNotBlank()) { inputPassword.length >= 6 } else { false }
    fun isvalid() = isvalidPassword() && isvalidEmail() && isValidUsername()
    fun isValidSignIn(): Boolean {
        return inputEmail.isNotBlank() && inputPassword.isNotBlank()
    }
    //YARDIMCI METODLAR (UI Destekleri)
    fun emailSupportText() = !isvalidEmail() && inputEmail.isNotBlank()
    fun passwordSupportText() = !isvalidPassword() && inputPassword.isNotBlank()
    fun usernameSupportText() = !isValidUsername() && inputUsername.isNotBlank()

    var passwordVisibility by mutableStateOf(false)

    fun resetInputs() {
        inputEmail = ""
        inputPassword = ""
        inputUsername = ""
    }
}