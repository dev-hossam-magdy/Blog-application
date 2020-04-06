package com.example.blogapplication.di

import com.example.blogapplication.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitybuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity():AuthActivity
}