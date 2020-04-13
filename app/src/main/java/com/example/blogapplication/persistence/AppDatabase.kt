package com.example.blogapplication.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.persistence.daos.AuthTokenDao

@Database(entities = [AccountProperties::class,AuthToken::class],version = 2 ,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun getAuthTokenDao():AuthTokenDao
    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object{
        const val DATABASE_NAME ="app_db"
    }


}