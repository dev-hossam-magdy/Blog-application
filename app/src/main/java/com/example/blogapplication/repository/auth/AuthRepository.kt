package com.example.blogapplication.repository.auth

import android.util.Log
import com.example.blogapplication.api.auth.OpenApiAuthService
import com.example.blogapplication.di.anotaion.AuthScope
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.persistence.daos.AuthTokenDao
import com.example.blogapplication.session.SessionManager
import javax.inject.Inject

@AuthScope
class AuthRepository @Inject constructor(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val openApiAuthService: OpenApiAuthService,
    private val sessionManager: SessionManager
) {
    val TAG = "AuthRepository"
    init {
        Log.e(TAG,"it is work ${sessionManager.hashCode()}")

    }


    fun login(username:String ,password:String) = openApiAuthService.login(username , password)

    fun register(email:String , username: String , password: String , password2: String) =
        openApiAuthService.register(email,username,password,password2)



}