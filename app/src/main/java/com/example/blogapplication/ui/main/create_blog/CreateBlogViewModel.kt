package com.example.blogapplication.ui.main.create_blog

import android.icu.text.CaseMap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.blogapplication.repository.main.CreateBlogRepository
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.BaseViewModel
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.main.blog.state.BlogPostViewState
import com.example.blogapplication.ui.main.create_blog.state.CreateBlogStateEvent
import com.example.blogapplication.ui.main.create_blog.state.CreateBlogViewState
import com.example.blogapplication.util.AbsentLiveData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CreateBlogViewModel
@Inject
constructor(
    private val createBlogRepository: CreateBlogRepository,
    private val sessionManager: SessionManager
) : BaseViewModel<CreateBlogStateEvent, CreateBlogViewState>() {

    override fun initNewState(): CreateBlogViewState = CreateBlogViewState()

    override fun handelStatEvent(stateEvent: CreateBlogStateEvent): LiveData<DataState<CreateBlogViewState>> =
        when (stateEvent) {
            is CreateBlogStateEvent.CreateNewBlogStateEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    val title = stateEvent.title.toRequestBody("text/plain".toMediaTypeOrNull())
                    val body = stateEvent.body.toRequestBody("text/plain".toMediaTypeOrNull())
                    createBlogRepository.createNewBlogPost(authToken, title, body, stateEvent.image)

                } ?: AbsentLiveData.create()
            }
            is CreateBlogStateEvent.None ->
                liveData<DataState<CreateBlogViewState>> {
                    emit(
                        DataState.Data(data = null, response = null)
                    )
                }

        }

    fun setNewBlogFields(title: String?, body: String?, imageUri: Uri?) {
        val update = getCurrenViewStateOrNew()
        val newBlogFields = update.newBlogFields
        title?.let { newBlogFields.newBlogTitle = it }
        body?.let { newBlogFields.newBlogBody = it }
        imageUri?.let { newBlogFields.newBlogImageUri = it }
        update.newBlogFields = newBlogFields
        setViewState(update)
    }

    fun clearNewBlogFields() {
        val update = getCurrenViewStateOrNew()
        update.newBlogFields = CreateBlogViewState.NewBlogFields()
        setViewState(update)
    }

    fun cancelActiveJobs() {
        createBlogRepository.cancelActiveJobs()
        handelPendingData()
    }

    fun getImageUri() = getCurrenViewStateOrNew().newBlogFields.newBlogImageUri

    private fun handelPendingData() {
        setStateEvent(CreateBlogStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}