package com.example.blogapplication.session

import android.app.Application
import androidx.room.Index
import com.example.blogapplication.persistence.daos.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val authTokenDao: AuthTokenDao,
    private val application: Application
) {


}