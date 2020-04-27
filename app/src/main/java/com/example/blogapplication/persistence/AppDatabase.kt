package com.example.blogapplication.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.persistence.daos.AuthTokenDao
import com.example.blogapplication.persistence.daos.BlogPostDao

@Database(entities = [AccountProperties::class,AuthToken::class , BlogPost::class],version = 3 ,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun getAuthTokenDao():AuthTokenDao
    abstract fun getAccountPropertiesDao(): AccountPropertiesDao
    abstract fun getBlogPostDao():BlogPostDao

    companion object{
        const val DATABASE_NAME ="app_db"
    }


}