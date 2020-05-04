package com.example.blogapplication.ui.main.create_blog.state

import okhttp3.MultipartBody

sealed class CreateBlogStateEvent {

    data class CreateNewBlogStateEvent(
        val title:String,
        val body:String,
        var image :MultipartBody.Part
    ):CreateBlogStateEvent()

    class None():CreateBlogStateEvent()
}