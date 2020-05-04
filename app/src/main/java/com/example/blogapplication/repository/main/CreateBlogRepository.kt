package com.example.blogapplication.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.blogapplication.api.main.OpenAPiMainService
import com.example.blogapplication.api.main.response.BlogCreateUpdateResponse
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.models.BlogPost
import com.example.blogapplication.persistence.daos.AuthTokenDao
import com.example.blogapplication.persistence.daos.BlogPostDao
import com.example.blogapplication.repository.JobManager
import com.example.blogapplication.repository.NetworkBoundResourse
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.Response
import com.example.blogapplication.ui.ResponseType
import com.example.blogapplication.ui.convertServerStringDateToLong
import com.example.blogapplication.ui.main.create_blog.state.CreateBlogViewState
import com.example.blogapplication.util.AbsentLiveData
import com.example.blogapplication.util.GenericApiResponse
import com.example.blogapplication.util.SuccessHandling
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject

class CreateBlogRepository
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val openAPiMainService: OpenAPiMainService,
    private val blogPostDao: BlogPostDao
) : JobManager("CreateBlogRepository") {
    private val TAG = "CreateBlogRepository"


    fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?
    ): LiveData<DataState<CreateBlogViewState>> =
        object : NetworkBoundResourse<BlogCreateUpdateResponse, BlogPost, CreateBlogViewState>
            (
            isNetworkRequest = true,
            shouldCanceledIfNoNetworkConnection = true,
            shouldLoadCachedData = false,
            isNetworkAvailable = sessionManager.isConnectedToTheInternet()
        ) {
            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogCreateUpdateResponse>) {

                if (response.body.response.equals(SuccessHandling.SUCCESS_BLOG_CREATED)){
                    response.body.let { blogCreateUpdateResponse ->
                        updatedLocalDataBase(
                            BlogPost(
                                pk = blogCreateUpdateResponse.pk,
                                body = blogCreateUpdateResponse.body,
                                title = blogCreateUpdateResponse.title,
                                image = blogCreateUpdateResponse.image,
                                username = blogCreateUpdateResponse.username,
                                dateUpdate = blogCreateUpdateResponse.date_updated.convertServerStringDateToLong(),
                                slue = blogCreateUpdateResponse.slug
                            )
                        )

                        onCompleteJob(DataState.Data(
                            data = null,
                            response = Response(SuccessHandling.SUCCESS_BLOG_CREATED,ResponseType.Dialog())
                        ))
                    }

                }else
                    onCompleteJob(DataState.Data(
                        data = null,
                        response = Response(response.body.response,ResponseType.Dialog())
                    ))

            }

            // not needed oin this case
            override suspend fun makeCachedRequestAndReturn() {

            }

            // not needed oin this case
            override fun loadCachedData(): LiveData<CreateBlogViewState> =
                AbsentLiveData.create()

            override suspend fun updatedLocalDataBase(cachedObject: BlogPost?) {
                cachedObject?.let {
                    blogPostDao.updateBlogPost(
                        it.pk,
                        it.title,
                        it.body,
                        it.image
                    )
                }

            }

            override fun createCall(): LiveData<GenericApiResponse<BlogCreateUpdateResponse>> =
                openAPiMainService.createBlog("Token ${authToken.token}", title, body, image)

            override fun setJob(job: Job) {
                addJob("createNewBlogPost", job)
            }
        }.getResultAsLiveData()

}