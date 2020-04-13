package com.example.blogapplication.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.blogapplication.api.auth.OpenApiAuthService
import com.example.blogapplication.api.network_response.LoginResponse
import com.example.blogapplication.api.network_response.RegistrationResponse
import com.example.blogapplication.di.anotaion.AuthScope
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.persistence.daos.AccountPropertiesDao
import com.example.blogapplication.persistence.daos.AuthTokenDao
import com.example.blogapplication.repository.NetworkBoundResourse
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.Response
import com.example.blogapplication.ui.ResponseType
import com.example.blogapplication.ui.auth.state.AuthViewState
import com.example.blogapplication.ui.auth.state.LoginFields
import com.example.blogapplication.ui.auth.state.RegistarationFields
import com.example.blogapplication.util.*
import kotlinx.coroutines.Job
import javax.inject.Inject

@AuthScope
class AuthRepository @Inject constructor(
    private val authTokenDao: AuthTokenDao,
    private val accountPropertiesDao: AccountPropertiesDao,
    private val openApiAuthService: OpenApiAuthService,
    private val sessionManager: SessionManager,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesEditor: SharedPreferences.Editor
) {
    val TAG = "AuthRepository"
    private var repoJob: Job? = null

    init {
        Log.e(TAG, "it is work ${sessionManager.hashCode()}")

    }

    fun attempLogin(email: String, password: String): LiveData<DataState<AuthViewState>> {
        val loginFieldsErrors = LoginFields(email, password).isValidForLogin()
        if (!loginFieldsErrors.equals(LoginFields.LoginError.none()))
            return returnErrorResponse(loginFieldsErrors, ResponseType.Dialog())

        return object :
            NetworkBoundResourse<LoginResponse, AuthViewState>(
                sessionManager.isConnectedToTheInternet(),
                true
            ) {
            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<LoginResponse>) {
                Log.e(TAG, "handelApiSucessResponse.... ${response}")
                if (response.body.response.equals(ErrorHandling.GENERIC_AUTH_ERROR))
                    return onErrorReturn(ErrorHandling.GENERIC_AUTH_ERROR, true, false)

                addAccountToLocalCach(
                    pk = response.body.pk,
                    username = response.body.email,
                    email = response.body.email
                )

                val result = addTokenToLocalCach(
                    accountPk = response.body.pk,
                    token = response.body.token
                )
                if (result < 0)
                    return onCompleteJob(
                        DataState.Error(
                            response = Response(
                                ErrorHandling.ERROR_SAVE_AUTH_TOKEN,
                                ResponseType.Dialog()
                            )
                        )
                    )

                saveAuthUserToPrefs(response.body.email)
                onCompleteJob(
                    DataState.Data(
                        data = AuthViewState(
                            authToken = AuthToken(
                                response.body.pk,
                                response.body.token
                            )
                        ),
                        response = null
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
            }

            override fun setjob(job: Job) {
                repoJob?.cancel()
                repoJob = job
            }

            // not needed in this case
            override fun makeCachedRequest() {

            }
        }.getResultAsLiveData()

    }


    fun attempRegistration(
        email: String,
        password: String,
        confirmPassword: String,
        username: String
    ): LiveData<DataState<AuthViewState>> {
        val registrationFieldsError =
            RegistarationFields(email, username, password, confirmPassword).isValidForRegistration()
        if (!registrationFieldsError.equals(RegistarationFields.RegistrationError.none()))
            return returnErrorResponse(registrationFieldsError, ResponseType.Dialog())
        return object :
            NetworkBoundResourse<RegistrationResponse, AuthViewState>(
                sessionManager.isConnectedToTheInternet(),
                true
            ) {
            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<RegistrationResponse>) {
                Log.e(TAG, "handelApiSucessResponse.... ${response}")
                if (response.body.response.equals(ErrorHandling.GENERIC_AUTH_ERROR))
                    return onErrorReturn(ErrorHandling.ERROR_SAVE_AUTH_TOKEN, true, false)
                Log.e(TAG, "handelApiSuccessResponse.... onCompleteJob:will colled ")
                addAccountToLocalCach(
                    pk = response.body.pk,
                    username = response.body.email,
                    email = response.body.email
                )

                val result = addTokenToLocalCach(
                    accountPk = response.body.pk,
                    token = response.body.token
                )
                if (result < 0)
                    return onCompleteJob(
                        dataState = DataState.Error(
                            Response(ErrorHandling.ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                saveAuthUserToPrefs(response.body.email)
                onCompleteJob(
                    DataState.Data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.pk, response.body.token)
                        ),
                        response = null
                    )
                )

            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setjob(job: Job) {
                cancelActivesJob()
                repoJob = job
            }

            // not needed in this case
            override fun makeCachedRequest() {

            }
        }.getResultAsLiveData()

    }

    private fun returnErrorResponse(
        loginFieldsErrors: String,
        dialog: ResponseType.Dialog
    ): LiveData<DataState<AuthViewState>> =
        object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                Log.e(TAG, "returnErrorResponse.... ${loginFieldsErrors}")
                value = DataState.Error(
                    response = Response(
                        message = loginFieldsErrors,
                        responseType = dialog
                    )
                )
            }
        }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>> {
        val previousUserEmail: String? =
            sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)
        if (previousUserEmail == null) {
            Log.e(TAG, "checkPreviousAuthUser an d the value is null")
            return returnNoTokenFound()
        }

        return object : NetworkBoundResourse<Void, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            false
        ) {
            // not used in this case
            override suspend fun handelApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<Void>) {

            }


            override fun makeCachedRequest() {
                Log.e(TAG, "makeCachedRequest:")
                accountPropertiesDao.searchByEmail(previousUserEmail).let { accountProperties ->
                    accountProperties?.let { accountProperties ->
                        if (accountProperties.pk > -1)
                            authTokenDao.searchbyPK(accountProperties.pk)?.let { authToken ->
                                authToken?.let {
                                    onCompleteJob(
                                        DataState.Data(
                                            data = AuthViewState(
                                                authToken = authToken
                                            ),
                                            response = null
                                        )
                                    )
                                    return
                                }
                            }
                    }

                    Log.e(TAG, "checkPreviousAuthUser:  Auth token not found...")
                    onCompleteJob(
                        dataState = DataState.Data(
                            data = null, response = Response(
                                message = previousUserEmail,
                                responseType = ResponseType.None()
                            )
                        )
                    )

                }


            }

            // not used in this case
            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setjob(job: Job) {
                repoJob?.cancel()
                repoJob = job
            }
        }.getResultAsLiveData()
    }

    private fun returnNoTokenFound(): LiveData<DataState<AuthViewState>> =
        object : LiveData<DataState<AuthViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.Data(
                    response = Response(
                        SuccessHandling.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                        ResponseType.None()
                    ),
                    data = null
                )
            }
        }


    private fun addAccountToLocalCach(email: String, username: String, pk: Int) {
        accountPropertiesDao.insertOrIgnore(
            AccountProperties(pk, email, username)
        )
    }


    private fun addTokenToLocalCach(token: String, accountPk: Int): Long {
        val result = authTokenDao.insert(
            AuthToken(accountPk, token)
        )
        return result
    }

    fun cancelActivesJob() {
        Log.e(TAG, "cancelActivesJob....")
        repoJob?.cancel()
    }


    private fun saveAuthUserToPrefs(email: String) {
        sharedPreferencesEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharedPreferencesEditor.apply()

    }

    fun login(username: String, password: String) = openApiAuthService.login(username, password)

    fun register(email: String, username: String, password: String, password2: String) =
        openApiAuthService.register(email, username, password, password2)


}