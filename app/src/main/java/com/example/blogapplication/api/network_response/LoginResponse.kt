package com.example.blogapplication.api.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginResponse(

    @SerializedName("response")
    @Expose
    var response: String,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String,

    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("pk")
    @Expose
    var pk: Int,

    @SerializedName("email")
    @Expose
    var email: String
)
{
    override fun toString(): String {
        return "LoginResponse(response='$response', errorMessage='$errorMessage', token='$token', pk=$pk, email='$email')"
    }
}