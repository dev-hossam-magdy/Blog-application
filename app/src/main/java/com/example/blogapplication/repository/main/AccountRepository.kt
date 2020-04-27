package com.example.blogapplication.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.example.blogapplication.api.GenericResponse
import com.example.blogapplication.api.main.OpenAPiMainService
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.repository.JobManager
import com.example.blogapplication.repository.NetworkBoundResourse
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.Response
import com.example.blogapplication.ui.ResponseType
import com.example.blogapplication.ui.main.account.state.AccountViewState
import com.example.blogapplication.util.AbsentLiveData
import com.example.blogapplication.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val openAPiMainService: OpenAPiMainService,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val sessionManager: SessionManager
) :JobManager("AccountRepository"){
    private val TAG = "AccountRepository"

    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>> =
        object : NetworkBoundResourse<AccountProperties, AccountProperties, AccountViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {

            override fun loadCachedData(): LiveData<AccountViewState> =
                accountPropertiesDao.searchByPk(authToken.accountPk ?: -1).switchMap {
                    return@switchMap object : LiveData<AccountViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value = AccountViewState(it)
                        }
                    }
                }


            override suspend fun updatedLocalDataBase(cachedObject: AccountProperties?) {
                cachedObject?.let {
                    accountPropertiesDao.updateAccountProperties(
                        it.pk,
                        it.username,
                        it.email
                    )
                }

            }

            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<AccountProperties>) {

                updatedLocalDataBase(response.body)
                makeCachedRequest()
            }

            override suspend fun makeCachedRequest() {
                withContext(Main) {
                    result.addSource(loadCachedData()) {
                        onCompleteJob(
                            DataState.Data(
                                data = it,
                                response = null
                            )
                        )
                    }
                }

            }

            override fun createCall(): LiveData<GenericApiResponse<AccountProperties>> =
                openAPiMainService.getAccountProperties("Token ${authToken.token}")

            override fun setjob(job: Job) {
                addJob("getAccountProperties",job)
            }
        }.getResultAsLiveData()

    fun saveAccountProperties(
        authToken: AuthToken,
        accountProperties: AccountProperties
    ): LiveData<DataState<AccountViewState>> =
        object :
            NetworkBoundResourse<GenericResponse, Any, AccountViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                true,
                false
            ) {
            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {
                withContext(Main) {
                    response.body?.let { genericResponse ->
                        onCompleteJob(
                            DataState.Data(
                                data = null,
                                response = Response(
                                    message = genericResponse.response,
                                    responseType = ResponseType.Toast()
                                )
                            )
                        )

                    }
                }
            }

            // not needed in this case
            override suspend fun makeCachedRequest() {
                //TODO("Not yet implemented")
            }

            // not needed in this case
            override fun loadCachedData(): LiveData<AccountViewState> {

                return AbsentLiveData.create()
            }

            // not needed in this case
            override suspend fun updatedLocalDataBase(cachedObject: Any?) {
                accountPropertiesDao.updateAccountProperties(
                    pk = accountProperties.pk,
                    email = accountProperties.email,
                    username = accountProperties.username
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openAPiMainService.saveAccountProperties(
                    "Token ${authToken.token ?: ""}",
                    accountProperties.username,
                    accountProperties.email
                )
            }

            override fun setjob(job: Job) {
               addJob("saveAccountProperties",job)
            }
        }.getResultAsLiveData()

    fun changePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): LiveData<DataState<AccountViewState>> =
        object : NetworkBoundResourse<GenericResponse, Any, AccountViewState>(
            isNetworkAvailable = sessionManager.isConnectedToTheInternet(),
            isNetworkRequest = true,
            shouldCanceledIfNoNetworkConnection = true,
            shouldLoadCachedData = false
        ) {
            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {
                withContext(Main) {
                    response.body?.let { genericResponse ->
                        onCompleteJob(
                            DataState.Data(
                                data = null,
                                response = Response(genericResponse.response, ResponseType.Toast())
                            )
                        )
                    }
                }
            }

            // not needed in this case
            override suspend fun makeCachedRequest() {
            }

            // not needed in this case

            override fun loadCachedData(): LiveData<AccountViewState> =
                AbsentLiveData.create()

            // not needed in this case
            override suspend fun updatedLocalDataBase(cachedObject: Any?) {

            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> =
                openAPiMainService.changePassword(
                    authorization = "Token ${authToken.token}",
                    currentPassword = currentPassword,
                    confirmNewPassword = confirmNewPassword,
                    newPassword = newPassword
                )


            override fun setjob(job: Job) {
                addJob("changePassword",job)
            }
        }.getResultAsLiveData()


}