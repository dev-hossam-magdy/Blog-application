package com.example.blogapplication.ui.main.blog.viewmodels

import com.example.blogapplication.models.BlogPost

fun BlogPostViewModel.getPageNumber():Int =
    getCurrenViewStateOrNew().blogFields.pageNumber

fun BlogPostViewModel.getsQueryInProgress():Boolean =
    getCurrenViewStateOrNew().blogFields.isQueryInProgress

fun BlogPostViewModel.getIsQueryExhausted():Boolean =
    getCurrenViewStateOrNew().blogFields.isQueryExhausted

fun BlogPostViewModel.getFilter():String =
    getCurrenViewStateOrNew().blogFields.filter

fun BlogPostViewModel.getOrder():String =
    getCurrenViewStateOrNew().blogFields.order

fun BlogPostViewModel.getSlug():String =
    getCurrenViewStateOrNew().viewBlogFields.blogPost?.slue?:""

fun BlogPostViewModel.getIsAuthorOfBlogPost(): Boolean =
    getCurrenViewStateOrNew().viewBlogFields.isAuthorOfBlogPost

fun BlogPostViewModel.getBlogPost() =
    getCurrenViewStateOrNew().viewBlogFields.blogPost?:getDummyBlogPost()

private fun getDummyBlogPost(): BlogPost =
    BlogPost(-1,"","","","",-1,"")
