package com.example.blogapplication.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.example.blogapplication.api.main.OpenAPiMainService
import com.example.blogapplication.api.main.response.BlogListSearchResponse
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.persistence.daos.BlogPostDao
import com.example.blogapplication.repository.JobManager
import com.example.blogapplication.repository.NetworkBoundResourse
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.main.blog.state.BlogPostViewState
import com.example.blogapplication.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlogPostRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val blogPostDao: BlogPostDao,
    private val openAPiMainService: OpenAPiMainService
) : JobManager("BlogPostRepository") {
    private val TAG = "JobManager"


    fun searchBlogPost(
        authToken: AuthToken,
        query: String
    ): LiveData<DataState<BlogPostViewState>> {
        return object :
            NetworkBoundResourse<BlogListSearchResponse, List<BlogPost>, BlogPostViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogListSearchResponse>) {
                updatedLocalDataBase(response.body.results)
                makeCachedRequest()
            }

            override suspend fun makeCachedRequest() {
                withContext(Main){
                    result.addSource(loadCachedData()){ BlogPostViewState ->
                        onCompleteJob(DataState.Data(BlogPostViewState,null))
                    }
                }

            }

            override fun loadCachedData(): LiveData<BlogPostViewState> =
                blogPostDao.getAllBlogPosts().switchMap { blogPosts ->
                    return@switchMap object : LiveData<BlogPostViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value = BlogPostViewState(
                                blogFields = BlogPostViewState.BlogFields(
                                    blogList = blogPosts, searchQuery = query
                                )
                            )
                        }
                    }
                }


            override suspend fun updatedLocalDataBase(cachedObject: List<BlogPost>?) {
                cachedObject?.let {
                    withContext(IO) {
                        Log.e(TAG, "the blog posts will cached")
                        blogPostDao.insert(cachedObject)
                    }
                }

            }

            override fun createCall(): LiveData<GenericApiResponse<BlogListSearchResponse>> =
                openAPiMainService.searchListBlogPost(
                    authorization = "Token ${authToken.token}",
                    query = query
                )


            override fun setjob(job: Job) {
                addJob("searchBlogPost", job)

            }
        }.getResultAsLiveData()
    }
}