package com.example.blogapplication.ui.main.blog.viewmodels

fun BlogPostViewModel.getPageNumber():Int =
    getCurrenViewStateOrNew().blogFields.pageNumber

fun BlogPostViewModel.getsQueryInProgress():Boolean =
    getCurrenViewStateOrNew().blogFields.isQueryInProgress

fun BlogPostViewModel.getIsQueryExhausted():Boolean =
    getCurrenViewStateOrNew().blogFields.isQueryExhausted