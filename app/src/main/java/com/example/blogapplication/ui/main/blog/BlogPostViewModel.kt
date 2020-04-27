package com.example.blogapplication.ui.main.blog

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Query
import com.bumptech.glide.RequestManager
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.repository.main.BlogPostRepository
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.BaseViewModel
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import com.example.blogapplication.ui.main.blog.state.BlogPostViewState
import com.example.blogapplication.util.AbsentLiveData
import javax.inject.Inject

class BlogPostViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val blogPostRepository: BlogPostRepository,
    private val requestManager: RequestManager,
    private val sharedPreferences: SharedPreferences

) : BaseViewModel<BlogPostStateEvent, BlogPostViewState>() {

    private val TAG = "BlogPostViewModel"

    init {
        Log.e(TAG, "BlogPostViewModel created")
    }

    override fun initNewState(): BlogPostViewState =
        BlogPostViewState()

    override fun handelStatEvent(stateEvent: BlogPostStateEvent): LiveData<DataState<BlogPostViewState>> =
        when (stateEvent) {
            is BlogPostStateEvent.BlogSearchEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    blogPostRepository.searchBlogPost(
                        authToken = authToken,
                        query = stateEvent.query
                    )

                } ?: AbsentLiveData.create()

            }
            is BlogPostStateEvent.CheckAuthorOfBlogPost -> {
                AbsentLiveData.create()
            }
            is BlogPostStateEvent.None -> {
                AbsentLiveData.create()
            }
        }

    fun setQuery(query: String) {
        val update = getCurrenViewStateOrNew()
        if (update.blogFields.searchQuery.equals(query))
            return
        update.blogFields.searchQuery = query
        _viewState.value = update
    }

    fun setBlogList(blogList: List<BlogPost>) {
        val update = getCurrenViewStateOrNew()

        if (update.blogFields.blogList == blogList)
            return
        update.blogFields.blogList = blogList
        _viewState.value = update
    }

    fun cancelActiveJobs() {
        blogPostRepository.cancelActiveJobs()
        handelPendingData()
    }

    private fun handelPendingData() {
        setStateEvent(BlogPostStateEvent.None())
    }

    fun setBlogPost(blogPost: BlogPost) {
        val update = getCurrenViewStateOrNew()
        if (update.viewBlogFields.blogPost == blogPost)
            return
        update.viewBlogFields.blogPost = blogPost
        _viewState.value = update

    }

    fun setAuthorOfBlogPost(isAuthorOfBlogPost: Boolean){
        val update = getCurrenViewStateOrNew()
        if (update.viewBlogFields.isAuthorOfBlogPost == isAuthorOfBlogPost)
            return
        update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
        _viewState.value = update
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}