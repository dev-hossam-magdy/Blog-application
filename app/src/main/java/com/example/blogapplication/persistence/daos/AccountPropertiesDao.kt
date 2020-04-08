package com.example.blogapplication.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blogapplication.models.AccountProperties


@Dao
interface AccountPropertiesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(accountProperties: AccountProperties):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties):Long

    @Query("SELECT * FROM account_properties WHERE pk =:Pk")
    fun searchByPk(Pk:Int):AccountProperties?

    @Query("SELECT * FROM account_properties WHERE email =:email")
    fun searchByEmail(email: String):AccountProperties?

}