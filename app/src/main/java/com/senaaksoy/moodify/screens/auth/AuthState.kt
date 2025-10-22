package com.senaaksoy.moodify.screens.auth

enum class AuthState {
    EMPTY,
    SUCCESS,
    FAILURE,
    USER_ALREADY_EXISTS,
    INVALID_CREDENTIALS,
    INVALID_EMAIL_OR_PASSWORD,
    EMAIL_NOT_VERIFIED,
    PASSWORD_RESET_EMAIL_SENT

}