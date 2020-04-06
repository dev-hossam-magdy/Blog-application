package com.example.blogapplication.di

import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    fun provideString():String="welcom in dagger"
}