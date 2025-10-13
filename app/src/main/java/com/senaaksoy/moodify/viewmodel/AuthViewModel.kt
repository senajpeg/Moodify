package com.senaaksoy.moodify.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.senaaksoy.moodify.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var inputEmail by mutableStateOf("")
        private set

    var inputPassword by mutableStateOf("")
        private set

    fun updateInputPassword(password: String) {
        inputPassword = password
    }

    fun updateInputEmail(email: String) {
        inputEmail = email
    }


}