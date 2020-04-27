package com.example.blogapplication.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.blogapplication.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAuthFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this,factory).get(AuthViewModel::class.java)
        }?: throw Exception("the activity not created")
        viewModel.cancelActiveJobs()
    }
}