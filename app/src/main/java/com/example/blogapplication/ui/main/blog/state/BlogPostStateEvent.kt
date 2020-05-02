package com.example.blogapplication.ui.main.blog.state

import android.icu.text.CaseMap
import android.media.Image
import okhttp3.MultipartBody

sealed class BlogPostStateEvent {

    class BlogSearchEvent(val query:String):BlogPostStateEvent()

    class CheckAuthorOfBlogPost:BlogPostStateEvent()

    class DeleteBlogPostEvent:BlogPostStateEvent()

    data class UpdatedBlogPostEvent(
        var title: String,
        var body :String,
        var image: MultipartBody.Part?
    ):BlogPostStateEvent()

    class None:BlogPostStateEvent()

}