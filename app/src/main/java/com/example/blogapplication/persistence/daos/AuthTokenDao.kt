package com.example.blogapplication.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blogapplication.models.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authToken: AuthToken):Long

    @Query("UPDATE auth_token SET token =null WHERE account_pk=:pk")
    fun nullifyToken(pk:Int):Int

    @Query("SELECT * FROM auth_token WhERE account_pk=:pk")
    fun searchbyPK(pk:Int):AuthToken?

}