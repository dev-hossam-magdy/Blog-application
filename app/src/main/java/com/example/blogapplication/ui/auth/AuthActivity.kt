package com.example.blogapplication.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.blogapplication.R
import com.example.blogapplication.ViewModelProviderFactory
import com.example.blogapplication.base.BaseActivity
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.ResponseType
import com.example.blogapplication.ui.auth.state.AuthStateEvent
import com.example.blogapplication.ui.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    override val TAG = "AuthActivity"
    override val progressBar: ProgressBar
        get() = authProgressBar

    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)
        Log.e(TAG, "the viewModel is ${viewModel.hashCode()} ")
        subscribeObservers()
        checkPreviousAuthUser()
    }

    override fun subscribeObservers() {
        Log.e(TAG, "in subscribeObservers")
        subscribeViewState()
        subscribeCachedToken()

        viewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChange(dataState)
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { viewState ->
                    viewState.authToken?.let { token ->
                        viewModel.setAuthToken(token)
                    }
                }

            }

        })


    }
    private fun checkPreviousAuthUser(){
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousEvent())
    }

    private fun subscribeCachedToken() {
        sessionManager.cachedToken.observe(this, Observer { authToken ->
            if (authToken != null && authToken.token != null && authToken.accountPk != -1) {
                Log.e(TAG, "subscribeObservers: sessionManager: authToken: user is authenticated")
                navigateToMainActivity()
            }
        })
    }

    private fun subscribeViewState() {
        viewModel.viewState.observeForever { AuthViewState ->
            Log.e(TAG, "it is traggered observeForever")
            AuthViewState.authToken?.let {
                Log.e(TAG, "it is traggered observeForever and session manger nor null")
                sessionManager.login(it)
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        viewModel.cancelActiveJops()
    }
}
