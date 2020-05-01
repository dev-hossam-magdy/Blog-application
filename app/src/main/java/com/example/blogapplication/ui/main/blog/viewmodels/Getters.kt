package com.example.blogapplication.ui.main.blog.viewmodels

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