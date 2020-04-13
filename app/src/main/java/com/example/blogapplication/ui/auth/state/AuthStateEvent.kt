package com.example.blogapplication.ui.auth.state

sealed class AuthStateEvent {

    data class LoginAttempEvent(
        val email: String,
        val password: String
    ) : AuthStateEvent()

    data class RegisterAttempsEvent(
        val email: String,
        val username: String,
        val password: String,
        val confirmPassword: String
    ):AuthStateEvent()

     class CheckPreviousEvent:AuthStateEvent()

}