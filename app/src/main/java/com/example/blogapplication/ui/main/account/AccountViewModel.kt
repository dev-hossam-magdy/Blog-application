package com.example.blogapplication.ui.main.account

import androidx.lifecycle.LiveData
import com.example.blogapplication.models.AccountProperties
import com.example.blogapplication.repository.main.AccountRepository
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.BaseViewModel
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.main.account.state.AccountStateEvent
import com.example.blogapplication.ui.main.account.state.AccountViewState
import com.example.blogapplication.util.AbsentLiveData
import javax.inject.Inject

class AccountViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val accountRepository: AccountRepository
) : BaseViewModel<AccountStateEvent, AccountViewState>() {


    override fun initNewState(): AccountViewState = AccountViewState()

    override fun handelStatEvent(stateEvent: AccountStateEvent): LiveData<DataState<AccountViewState>> =
        when (stateEvent) {
            is AccountStateEvent.ChangePasswordEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    authToken.token?.let {
                        accountRepository.changePassword(
                            authToken = authToken,
                            currentPassword = stateEvent.currentPassword,
                            newPassword = stateEvent.newPassword,
                            confirmNewPassword = stateEvent.confirmNewPassword
                        )
                    }

                } ?: AbsentLiveData.create()

            }
            is AccountStateEvent.GetAccountPropertiesEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    accountRepository.getAccountProperties(authToken)

                } ?: AbsentLiveData.create()
            }
            is AccountStateEvent.UpdateAccountPropertiesEvent -> {
                sessionManager.cachedToken.value?.let { authToken ->
                    authToken.accountPk?.let { pk ->
                        accountRepository.saveAccountProperties(
                            authToken, accountProperties = AccountProperties(
                                pk,
                                stateEvent.email,
                                stateEvent.username
                            )
                        )
                    }

                } ?: AbsentLiveData.create()
            }
            is AccountStateEvent.logoutEvent -> {
                logout()
                AbsentLiveData.create()
            }
            is AccountStateEvent.None -> {

                AbsentLiveData.create()
            }
        }

    fun setAccountPropertiesData(accountProperties: AccountProperties) {
        val update = getCurrenViewStateOrNew()
        if (update.accountProperties == accountProperties)
            return
        update.accountProperties = accountProperties
        _viewState.value = update
    }

    fun logout() {
        sessionManager.logout()
    }

    fun cancelActiveJobs() {
        accountRepository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
        setStateEvent(AccountStateEvent.None())
    }
}