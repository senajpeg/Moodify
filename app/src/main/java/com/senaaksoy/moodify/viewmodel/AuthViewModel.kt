package com.senaaksoy.moodify.viewmodel

import androidx.lifecycle.ViewModel
import com.senaaksoy.moodify.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

}