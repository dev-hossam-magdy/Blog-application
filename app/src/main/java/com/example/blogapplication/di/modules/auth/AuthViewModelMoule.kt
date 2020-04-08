package com.example.blogapplication.di.modules.auth

import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.di.auth.keys.ViewModelKey
import com.example.blogapplication.di.anotaion.AuthScope
import com.example.blogapplication.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelMoule {

    @AuthScope
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel):ViewModel

}