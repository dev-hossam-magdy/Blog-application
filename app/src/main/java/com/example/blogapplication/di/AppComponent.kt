package com.example.blogapplication.di

import android.app.Application
import com.example.blogapplication.base.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = arrayOf(
    AndroidInjectionModule::class,
    AppModule::class,
    ActivitybuilderModule::class
))
abstract class AppComponent :AndroidInjector<BaseApplication> {


    @Component.Factory
    interface factory{

        fun create(@BindsInstance application: Application):AppComponent

    }
}