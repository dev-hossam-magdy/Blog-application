package com.example.blogapplication.ui.main.blog.state

sealed class BlogPostStateEvent {

    class BlogSearchEvent(val query:String):BlogPostStateEvent()

    class CheckAuthorOfBlogPost:BlogPostStateEvent()
    class None:BlogPostStateEvent()

}