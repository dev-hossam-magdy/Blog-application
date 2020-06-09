package com.example.blogapplication.ui.main.blog.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.persistence.daos.BlogQueryUtils
import com.example.blogapplication.repository.main.BlogPostRepository
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.BaseViewModel
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.main.blog.state.BlogPostStateEvent
import com.example.blogapplication.ui.main.blog.state.BlogPostViewState
import com.example.blogapplication.util.AbsentLiveData
import com.example.blogapplication.util.PreferenceKeys
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class BlogPostViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val blogPostRepository: BlogPostRepository,
    private val editor: SharedPreferences.Editor,
    private val sharedPreferences: SharedPreferences

) : BaseViewModel<BlogPostStateEvent, BlogPostViewState>() {

    private val TAG = "BlogPostViewModel"

    init {
        setBlogFilter(
            sharedPreferences.getString(
                PreferenceKeys.BLOG_FILTER,
                BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            )
        )
        sharedPreferences.getString(
            PreferenceKeys.BLOG_ORDER,
            BlogQueryUtils.BLOG_ORDER_ASC
        )?.let {
            setBlogOrder(it)
        }
    }

    override fun initNewState(): BlogPostViewState =
        BlogPostViewState()

    override fun handelStatEvent(stateEvent: BlogPostStateEvent): LiveData<DataState<BlogPostViewState>> =
        when (stateEvent) {
            is BlogPostStateEvent.BlogSearchEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    blogPostRepository.searchBlogPost(
                        authToken = authToken,
                        query = stateEvent.query,
                        pageNumber = getPageNumber(),
                        filterAndOrder = getOrder() + getFilter()
                    )

                } ?: AbsentLiveData.create()

            }
            is BlogPostStateEvent.CheckAuthorOfBlogPost -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    blogPostRepository.isAuthorOfBlogPost(
                            authToken = authToken,
                            slug = getSlug()
                        )

                } ?:AbsentLiveData.create()
            }
            is BlogPostStateEvent.None -> {
                object : LiveData<DataState<BlogPostViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.Data(
                            null,
                            null
                        )
                    }
                }
            }

            is BlogPostStateEvent.DeleteBlogPostEvent ->{
                sessionManager.cachedToken.value?.let { authToken ->
                    blogPostRepository.deleteBlogPost(authToken,getBlogPost())
                }?:AbsentLiveData.create()

            }
            is BlogPostStateEvent.UpdatedBlogPostEvent ->{
                sessionManager.cachedToken.value?.let {authToken ->
                    val title = stateEvent.title.toRequestBody("text/plain".toMediaTypeOrNull())
                    val body = stateEvent.body.toRequestBody("text/plain".toMediaTypeOrNull())
                    blogPostRepository.updateBlogPost(
                        authToken = authToken,
                        image = stateEvent.image,
                        title = title,
                        body = body,
                        slug = getSlug()
                    )
                }?:AbsentLiveData.create()
            }

        }



    fun cancelActiveJobs() {
        blogPostRepository.cancelActiveJobs()
        handelPendingData()
    }

    private fun handelPendingData() {
        setStateEvent(BlogPostStateEvent.None())
    }


    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

    fun saveFilterOptions(filter: String, order: String) {
        editor.putString(PreferenceKeys.BLOG_FILTER, filter)
        editor.apply()
        editor.putString(PreferenceKeys.BLOG_ORDER, order)
        editor.apply()
    }

    fun removeDeletedBlogPost() {
        val update = getCurrenViewStateOrNew()
        val list = update.blogFields.blogList.dropWhile { blogPost ->
            blogPost == getBlogPost()
        }

        setBlogList(list)
    }



}