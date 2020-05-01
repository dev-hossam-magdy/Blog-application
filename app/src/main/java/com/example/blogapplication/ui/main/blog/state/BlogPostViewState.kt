package com.example.blogapplication.ui.main.blog.state

import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.persistence.daos.BlogQueryUtils

data class BlogPostViewState(
    // blog fragment data
    var blogFields: BlogFields = BlogFields(),

    // viewBlogFragments data
    var viewBlogFields: ViewBlogFields = ViewBlogFields()
) {

    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = "",
        var pageNumber:Int = 1,
        var isQueryInProgress:Boolean = false,
        var isQueryExhausted:Boolean = false,
        var filter:String = BlogQueryUtils.ORDER_BY_ASC_DATE_UPDATED,
        var order:String = BlogQueryUtils.BLOG_ORDER_ASC
    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false

    )
}