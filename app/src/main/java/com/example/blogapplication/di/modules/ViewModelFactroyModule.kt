package com.example.blogapplication.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.blogapplication.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactroyModule {
    @Binds
    abstract fun bindsAuthViewModelFactory(factort: ViewModelProviderFactory):ViewModelProvider.Factory
}