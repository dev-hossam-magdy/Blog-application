package com.example.blogapplication.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.blogapplication.models.AccountProperties


@Dao
interface AccountPropertiesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(accountProperties: AccountProperties):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties):Long

    @Query("SELECT * FROM account_properties WHERE pk =:Pk")
    fun searchByPk(Pk:Int):LiveData<AccountProperties>

    @Query("SELECT * FROM account_properties WHERE email =:email")
    fun searchByEmail(email: String):AccountProperties?

    @Query("UPDATE account_properties SET username = :username , email =:email WHERE pk = :pk")
    fun updateAccountProperties(pk:Int, username:String, email: String)

}