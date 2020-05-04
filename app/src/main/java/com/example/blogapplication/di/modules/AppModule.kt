package com.example.blogapplication.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.blogapplication.R
import com.example.blogapplication.persistence.AppDatabase
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.persistence.daos.AuthTokenDao
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.util.Constants
import com.example.blogapplication.util.LiveDataCallAdapterFactory
import com.example.blogapplication.util.PreferenceKeys
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRoomInctance(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()


    // it may be cahnded

    @Singleton
    @Provides
    fun provideAuthTokenDao(db: AppDatabase): AuthTokenDao =
        db.getAuthTokenDao()

    // it may be cahnded
    @Singleton
    @Provides
    fun provideAccountPropertiesDao(db:AppDatabase):AccountPropertiesDao =
        db.getAccountPropertiesDao()

    @Singleton
    @Provides
    fun provideRequestOptions():RequestOptions =
        RequestOptions()
            .placeholder(R.drawable.default_image)
            .error(R.drawable.default_image)
    @Singleton
    @Provides
    fun provideRequestManger(application: Application,requestOptions: RequestOptions):RequestManager =
        Glide.with(application)
            .setDefaultRequestOptions(requestOptions)


    @Singleton
    @Provides
    fun provideGsonBuilder():Gson =
        GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @Singleton
    @Provides
    fun provideRetrofitInctance(gson: Gson):Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application):SharedPreferences =
        application.getSharedPreferences(PreferenceKeys.APP_PREFERENCES,Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSharedPreferencesEditor(sharedPreferences: SharedPreferences):SharedPreferences.Editor =
        sharedPreferences.edit()




}