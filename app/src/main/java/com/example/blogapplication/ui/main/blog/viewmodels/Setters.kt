package com.example.blogapplication.ui.main.blog.viewmodels

import android.icu.text.CaseMap
import android.net.Uri
import com.example.blogapplication.models.BlogPost

fun BlogPostViewModel.setQuery(query: String) {
    val update = getCurrenViewStateOrNew()
    if (update.blogFields.searchQuery.equals(query))
        return
    update.blogFields.searchQuery = query
    setViewState(update)
}

fun BlogPostViewModel.setBlogList(blogList: List<BlogPost>) {
    val update = getCurrenViewStateOrNew()

    update.blogFields.blogList = blogList
    setViewState(update)
}


fun BlogPostViewModel.setBlogPost(blogPost: BlogPost) {
    val update = getCurrenViewStateOrNew()
    if (update.viewBlogFields.blogPost == blogPost)
        return
    update.viewBlogFields.blogPost = blogPost
    setViewState(update)

}

fun BlogPostViewModel.setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean) {
    val update = getCurrenViewStateOrNew()
    if (update.viewBlogFields.isAuthorOfBlogPost == isAuthorOfBlogPost)
        return
    update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
    setViewState(update)
}

fun BlogPostViewModel.setIsQueryInProgress(isQueryInProgress: Boolean) {
    val update = getCurrenViewStateOrNew()

    update.blogFields.isQueryInProgress = isQueryInProgress
    setViewState(update)
}

fun BlogPostViewModel.setIsQueryExhausted(isQueryExhausted: Boolean) {
    val update = getCurrenViewStateOrNew()

    update.blogFields.isQueryExhausted = isQueryExhausted
    setViewState(update)
}

fun BlogPostViewModel.setBlogFilter(filter: String?) {
    filter?.let {
        val update = getCurrenViewStateOrNew()
        if (update.blogFields.filter.equals(filter))
            return
        update.blogFields.filter = filter
        setViewState(update)
    }
}

fun BlogPostViewModel.setBlogOrder(order: String) {
    val update = getCurrenViewStateOrNew()
    if (update.blogFields.order.equals(order))
        return
    update.blogFields.order = order
    setViewState(update)
}

fun BlogPostViewModel.setUpdatedBlogFields(
    title: String?,
    imageUri: Uri?,
    body:String?
){
    val update = getCurrenViewStateOrNew()
    val updateBlogFields = update.updateBlogFields
    title?.let { updateBlogFields.updatedBlogTitle = it }
    imageUri?.let { updateBlogFields.updatedImageUri = it }
    body?.let { updateBlogFields.updatedBlogBody = it }
    update.updateBlogFields = updateBlogFields
    setViewState(update)
}

