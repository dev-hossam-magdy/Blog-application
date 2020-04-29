package com.example.blogapplication.ui.main.blog.viewmodels

import android.util.Log
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import com.example.blogapplication.ui.main.blog.state.BlogPostViewState

fun BlogPostViewModel.resetPage() {
    val update = getCurrenViewStateOrNew()
    update.blogFields.pageNumber = 1
    setViewState(update)

}

fun BlogPostViewModel.loadFirstPage() {
    setIsQueryExhausted(false)
    setIsQueryInProgress(true)
    resetPage()
    setStateEvent(BlogPostStateEvent.BlogSearchEvent(""))
}

fun BlogPostViewModel.incrementPageNumber() {
    val update = getCurrenViewStateOrNew()
    val page = update.copy().blogFields.pageNumber
    update.blogFields.pageNumber = page + 1
    setViewState(update)
}

fun BlogPostViewModel.loadNextPage() {
    Log.e("BlogPostViewModel", "loadNextPage")
    if (!getIsQueryExhausted() && !getsQueryInProgress()) {
        Log.e("BlogPostViewModel", "loadNextPage: the page will loading")
        incrementPageNumber()
        setIsQueryInProgress(true)
        setStateEvent(BlogPostStateEvent.BlogSearchEvent(""))
    }


}

fun BlogPostViewModel.handelIncomingBlogListData(blogPostViewState: BlogPostViewState) {
    val blogFields =  blogPostViewState.blogFields
    Log.e("BlogPostViewModel", "handelIncomingBlogListData: isQueryInProgress: ${blogFields.isQueryInProgress}")
    Log.e("BlogPostViewModel", "handelIncomingBlogListData: isQueryExhausted: ${blogFields.isQueryExhausted}")
    setIsQueryExhausted(blogFields.isQueryExhausted)
    setIsQueryInProgress(blogFields.isQueryInProgress)
    setBlogList(blogFields.blogList)
}

