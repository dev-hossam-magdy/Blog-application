package com.example.blogapplication.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.blogapplication.di.anotaion.AuthScope
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.repository.auth.AuthRepository
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
    private val authRepository: AuthRepository
) : BaseViewModel<AuthStateEvent, AuthViewState>() {


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
                AbsentLiveData.create()
            }
            is AuthStateEvent.CheckPreviousEvent -> {
                AbsentLiveData.create()
            }
            is AuthStateEvent.RegisterAttempsEvent -> {
                AbsentLiveData.create()
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

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrenViewStateOrNew()
        if (update.authToken == authToken)
            return
        update.authToken = authToken
        _viewState.value = update
    }

}