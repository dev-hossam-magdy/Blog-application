package com.example.blogapplication.repository.main

import android.media.Image
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.example.blogapplication.api.GenericResponse
import com.example.blogapplication.api.main.OpenAPiMainService
import com.example.blogapplication.api.main.response.BlogCreateUpdateResponse
import com.example.blogapplication.api.main.response.BlogListSearchResponse
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.persistence.daos.BlogPostDao
import com.example.blogapplication.persistence.daos.returnOrderedBlogQuery
import com.example.blogapplication.repository.JobManager
import com.example.blogapplication.repository.NetworkBoundResourse
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.Response
import com.example.blogapplication.ui.ResponseType
import com.example.blogapplication.ui.convertServerStringDateToLong
import com.example.blogapplication.ui.main.blog.state.BlogPostViewState
import com.example.blogapplication.util.AbsentLiveData
import com.example.blogapplication.util.Constants
import com.example.blogapplication.util.GenericApiResponse
import com.example.blogapplication.util.SuccessHandling
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class BlogPostRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val blogPostDao: BlogPostDao,
    private val openAPiMainService: OpenAPiMainService
) : JobManager("BlogPostRepository") {
    private val TAG = "JobManager"


    fun searchBlogPost(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        pageNumber: Int
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
                makeCachedRequestAndReturn()
            }

            override suspend fun makeCachedRequestAndReturn() {
                withContext(Main) {
                    result.addSource(loadCachedData()) { blogPostViewState ->
                        blogPostViewState.blogFields.isQueryInProgress = false
                        if (blogPostViewState.blogFields.blogList.size < pageNumber * Constants.PAGINATION_PAGE_SIZE)
                            blogPostViewState.blogFields.isQueryExhausted = true
                        onCompleteJob(DataState.Data(blogPostViewState, null))
                    }
                }

            }

            override fun loadCachedData(): LiveData<BlogPostViewState> =
                blogPostDao.returnOrderedBlogQuery(
                    query = query,
                    page = pageNumber,
                    filterAndOrder = filterAndOrder
                ).switchMap { blogPosts ->
                    return@switchMap object : LiveData<BlogPostViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value = BlogPostViewState(
                                blogFields = BlogPostViewState.BlogFields(
                                    blogList = blogPosts,
                                    searchQuery = query,
                                    isQueryInProgress = true
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
                    query = query,
                    page = pageNumber,
                    ordering = filterAndOrder
                )


            override fun setJob(job: Job) {
                addJob("searchBlogPost", job)

            }
        }.getResultAsLiveData()
    }

    fun isAuthorOfBlogPost(
        authToken: AuthToken,
        slug: String
    ): LiveData<DataState<BlogPostViewState>> =
        object : NetworkBoundResourse<GenericResponse, Any, BlogPostViewState>(
            isNetworkAvailable = sessionManager.isConnectedToTheInternet(),
            shouldLoadCachedData = false,
            shouldCanceledIfNoNetworkConnection = true,
            isNetworkRequest = true
        ) {

            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {
                Log.e(TAG, "handelApiSuccessResponse:")
                withContext(Main) {
                    response.body.response?.let {
                        var isAuthor = false
                        if (it.equals(SuccessHandling.RESPONSE_HAS_PERMISSION_TO_EDIT))
                            isAuthor = true
                        onCompleteJob(
                            dataState = DataState.Data(
                                data = BlogPostViewState(
                                    viewBlogFields = BlogPostViewState.ViewBlogFields(
                                        isAuthorOfBlogPost = isAuthor
                                    )
                                ),
                                response = null
                            )
                        )
                    }

                }

            }

            // not needed in this case
            override suspend fun makeCachedRequestAndReturn() {

            }

            // not needed in this case
            override fun loadCachedData(): LiveData<BlogPostViewState> = AbsentLiveData.create()


            // not needed in this case
            override suspend fun updatedLocalDataBase(cachedObject: Any?) {

            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> =
                openAPiMainService.isAuthorOfBlogPost("Token ${authToken.token}", slug)


            override fun setJob(job: Job) {
                addJob("isAuthorOfBlogPost", job)
            }
        }.getResultAsLiveData()


    fun deleteBlogPost(
        authToken: AuthToken,
        blogPost: BlogPost
    ): LiveData<DataState<BlogPostViewState>> =
        object : NetworkBoundResourse<GenericResponse, BlogPost, BlogPostViewState>(
            isNetworkRequest = true,
            shouldCanceledIfNoNetworkConnection = true,
            shouldLoadCachedData = false,
            isNetworkAvailable = sessionManager.isConnectedToTheInternet()
        ) {

            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {

                response.body.response?.let {
                    if (it.equals(SuccessHandling.SUCCESS_BLOG_DELETED))
                        updatedLocalDataBase(blogPost)
                    else
                        onCompleteJob(
                            dataState = DataState.Error(
                                response = Response(it, ResponseType.Toast())
                            )
                        )
                }

            }

            // not needed in this case
            override suspend fun makeCachedRequestAndReturn() {

            }

            // not needed in this case
            override fun loadCachedData(): LiveData<BlogPostViewState> = AbsentLiveData.create()

            override suspend fun updatedLocalDataBase(cachedObject: BlogPost?) {
                if (cachedObject != null) {
                    blogPostDao.deleteBlogPos(blogPost = cachedObject)
                    onCompleteJob(
                        DataState.Data(
                            data = null,
                            response = Response(
                                SuccessHandling.SUCCESS_BLOG_DELETED,
                                ResponseType.Toast()
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> =
                openAPiMainService.deleteBlogPost("Token ${authToken.token}", blogPost.slue)

            override fun setJob(job: Job) {
                addJob("deleteBlogPost", job)
            }
        }.getResultAsLiveData()

    fun updateBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        slug: String,
        image: MultipartBody.Part?
    ):LiveData<DataState<BlogPostViewState>> =
        object :NetworkBoundResourse<BlogCreateUpdateResponse , BlogPost , BlogPostViewState>(
            isNetworkAvailable = sessionManager.isConnectedToTheInternet(),
            shouldLoadCachedData = false ,
            shouldCanceledIfNoNetworkConnection = true,
            isNetworkRequest = true

        ){

            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogCreateUpdateResponse>) {
                if (response.body.response.equals(SuccessHandling.SUCCESS_BLOG_UPDATED)){
                    val body = response.body
                    val updatedBlogPost = BlogPost(
                        pk = body.pk,
                        body = body.body,
                        title = body.title,
                        image = body.image,
                        slue = body.slug,
                        dateUpdate = body.date_updated.convertServerStringDateToLong(),
                        username = body.username
                    )
                    updatedLocalDataBase(updatedBlogPost)
                    withContext(Main){
                        onCompleteJob(
                            DataState.Data(
                                data = BlogPostViewState(
                                    viewBlogFields = BlogPostViewState.ViewBlogFields(
                                        blogPost = updatedBlogPost
                                    )
                                ),
                                response = Response(body.response, ResponseType.Toast())
                            )
                        )
                    }
                }else
                    onCompleteJob(DataState.Data(null,
                        Response(response.body.response,ResponseType.Toast())
                    ))
            }

            // not needed in this case
            override suspend fun makeCachedRequestAndReturn() {

            }

            override fun loadCachedData(): LiveData<BlogPostViewState> = AbsentLiveData.create()

            override suspend fun updatedLocalDataBase(cachedObject: BlogPost?) {
                    blogPostDao.updateBlogPost(
                        pk = cachedObject?.pk?:-1,
                        image = cachedObject?.image?: "",
                        title = cachedObject?.title?:"" ,
                        body = cachedObject?.body?:""
                    )

            }

            override fun createCall(): LiveData<GenericApiResponse<BlogCreateUpdateResponse>> =
                openAPiMainService.updateBlogPost("Token ${authToken.token}",slug , title ,body , image)
            override fun setJob(job: Job) {
                addJob("updateBlogPost" , job)
            }
        }.getResultAsLiveData()
}