package com.example.blogapplication.di.modules.auth

import android.content.SharedPreferences
import com.example.blogapplication.api.auth.OpenApiAuthService
import com.example.blogapplication.di.anotaion.AuthScope
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.persistence.daos.AuthTokenDao
import com.example.blogapplication.repository.auth.AuthRepository
import com.example.blogapplication.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object AuthModule {

    @AuthScope
    @Provides
    fun provideOpenApiAuthService(retrofit: Retrofit.Builder): OpenApiAuthService =
        retrofit
            .build()
            .create(OpenApiAuthService::class.java)

    @AuthScope
    @Provides
    fun provideAuthAuthRepository(
        accountPropertiesDao: AccountPropertiesDao,
        authTokenDao: AuthTokenDao,
        openApiAuthService: OpenApiAuthService,
        sessionManager: SessionManager,
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor

    ): AuthRepository =
        AuthRepository(
            accountPropertiesDao =accountPropertiesDao,
            authTokenDao =authTokenDao,
            openApiAuthService =openApiAuthService,
            sessionManager =sessionManager,
            sharedPreferences =sharedPreferences ,
            sharedPreferencesEditor = editor
        )


}