package com.example.blogapplication.ui.main.blog.state

import com.example.blogapplication.models.BlogPost

data class BlogPostViewState(
    // blog fragment data
    var blogFields: BlogFields = BlogFields(),

    // viewBlogFragments data
    var viewBlogFields: ViewBlogFields = ViewBlogFields()
) {

    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = ""
    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false

    )
}