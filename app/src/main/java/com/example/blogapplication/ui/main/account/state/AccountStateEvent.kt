package com.example.blogapplication.ui.main.account.state


sealed class AccountStateEvent() {
    class GetAccountPropertiesEvent() : AccountStateEvent()

    data class UpdateAccountPropertiesEvent(
        val username: String,
        val email: String
    ) : AccountStateEvent()

    data class ChangePasswordEvent(
        val currentPassword:String,
        val newPassword:String,
        val confirmNewPassword:String
    ):AccountStateEvent()

    class None:AccountStateEvent()

    class logoutEvent:AccountStateEvent()

}