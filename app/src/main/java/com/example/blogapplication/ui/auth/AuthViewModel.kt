package com.example.blogapplication.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.blogapplication.di.anotaion.AuthScope
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.repository.auth.AuthRepository
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.BaseViewModel
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.auth.state.AuthStateEvent
import com.example.blogapplication.ui.auth.state.AuthViewState
import com.example.blogapplication.ui.auth.state.LoginFields
import com.example.blogapplication.ui.auth.state.RegistarationFields
import com.example.blogapplication.util.AbsentLiveData
import javax.inject.Inject

@AuthScope
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private var sessionManager: SessionManager
) : BaseViewModel<AuthStateEvent, AuthViewState>() {

    private val TAG = "AuthViewModel"



    fun login() =
        authRepository.login("magdyhossam03@gmail.com", "dukecs&nadaph89")

    fun register() =
        authRepository.register(
            email = "",
            username = "",
            password = "",
            password2 = ""
        )

    override fun initNewState(): AuthViewState = AuthViewState()

    override fun handelStatEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> =
        when (stateEvent) {
            is AuthStateEvent.LoginAttempEvent -> {
                Log.e(TAG,"handelStatEvent attempLogin ")
                authRepository.attempLogin(stateEvent.email, stateEvent.password)
            }
            is AuthStateEvent.CheckPreviousEvent -> {
                authRepository.checkPreviousAuthUser()
            }
            is AuthStateEvent.RegisterAttempsEvent -> {
                authRepository.attempRegistration(
                    stateEvent.email,
                    stateEvent.password,
                    stateEvent.confirmPassword,
                    stateEvent.username
                )
            }
        }

    fun setRegistrationFields(registarationFields: RegistarationFields) {
        val update = getCurrenViewStateOrNew()
        if (update.registrationFields == registarationFields)
            return
        update.registrationFields = registarationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrenViewStateOrNew()
        if (update.loginFields == loginFields)
            return
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken) {
        Log.e(TAG, "setAuthToken")
        val update = getCurrenViewStateOrNew()
        if (update.authToken == authToken)
            return
        update.authToken = authToken
        _viewState.value = update
    }

    fun cancelActiveJops(){
        authRepository.cancelActivesJob()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJops()
    }

}