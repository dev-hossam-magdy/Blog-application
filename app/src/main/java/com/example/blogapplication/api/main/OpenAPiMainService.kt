package com.example.blogapplication.api.main

import androidx.lifecycle.LiveData
import com.example.blogapplication.api.GenericResponse
import com.example.blogapplication.api.main.response.BlogListSearchResponse
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.util.Constants
import com.example.blogapplication.util.GenericApiResponse
import retrofit2.http.*

interface OpenAPiMainService {

    @GET("account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<AccountProperties>>

    @PUT("account/properties/update")
    @FormUrlEncoded
    fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("username") username: String,
        @Field("email") email: String
    ): LiveData<GenericApiResponse<GenericResponse>>

    @PUT("account/change_password/")
    @FormUrlEncoded
    fun changePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): LiveData<GenericApiResponse<GenericResponse>>

    @GET("blog/list")
    fun searchListBlogPost(
        @Header("Authorization") authorization: String,
        @Query("search") query: String,
        @Query("page") page:Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>

}