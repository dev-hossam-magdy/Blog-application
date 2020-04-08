package com.example.blogapplication.di

import android.app.Application
import com.example.blogapplication.base.BaseApplication
import com.example.blogapplication.di.modules.ActivitybuilderModule
import com.example.blogapplication.di.modules.AppModule
import com.example.blogapplication.di.modules.ViewModelFactroyModule
import com.example.blogapplication.session.SessionManager
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    AndroidInjectionModule::class,
    AppModule::class,
    ActivitybuilderModule::class,
    ViewModelFactroyModule::class
))
interface  AppComponent :AndroidInjector<BaseApplication> {

    val sessionManager:SessionManager;

    @Component.Factory
    interface factory{

        fun create(@BindsInstance application: Application):AppComponent

    }
}