package com.example.blogapplication.api.main

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.room.Delete
import com.example.blogapplication.api.GenericResponse
import com.example.blogapplication.api.main.response.BlogCreateUpdateResponse
import com.example.blogapplication.api.main.response.BlogListSearchResponse
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.util.Constants
import com.example.blogapplication.util.GenericApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Query("ordering") ordering:String,
        @Query("page") page:Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>

    @GET("blog/{slug}/is_author")
    fun isAuthorOfBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug :String
    ):LiveData<GenericApiResponse<GenericResponse>>

    @DELETE("blog/{slug}/delete")
    fun deleteBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug :String
    ):LiveData<GenericApiResponse<GenericResponse>>

    @Multipart
    @PUT("blog/{slug}/update")
    fun updateBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug :String,
        @Part("title") title:RequestBody,
        @Part("body") body:RequestBody,
        @Part image:MultipartBody.Part?
    ):LiveData<GenericApiResponse<BlogCreateUpdateResponse>>

    @Multipart
    @POST("blog/create")
    fun createBlog(
        @Header("Authorization") authorization: String,
        @Part("title") title:RequestBody,
        @Part("body") body:RequestBody,
        @Part image:MultipartBody.Part?
    ):LiveData<GenericApiResponse<BlogCreateUpdateResponse>>

}