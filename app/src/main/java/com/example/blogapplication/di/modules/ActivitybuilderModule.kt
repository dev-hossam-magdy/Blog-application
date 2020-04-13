package com.example.blogapplication.di.modules

import com.example.blogapplication.di.anotaion.AuthScope
import com.example.blogapplication.di.anotaion.MainScope
import com.example.blogapplication.di.modules.auth.AuthFragmentBuilderModule
import com.example.blogapplication.di.modules.auth.AuthModule
import com.example.blogapplication.di.modules.auth.AuthViewModelMoule
import com.example.blogapplication.ui.auth.AuthActivity
import com.example.blogapplication.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitybuilderModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthFragmentBuilderModule::class,
            AuthModule::class,
            AuthViewModelMoule::class
        ]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun ContributesMainActivity(): MainActivity
}