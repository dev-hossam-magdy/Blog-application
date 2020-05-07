package com.example.blogapplication.ui.main.account


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.blogapplication.R
import com.example.blogapplication.ViewModelProviderFactory
import com.example.blogapplication.ui.DataStateChangesListener
import com.example.blogapplication.ui.main.BaseMainFragment
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAccountFragment : BaseMainFragment() {

    val TAG: String = "AppDebug"

    @Inject
    lateinit var factory: ViewModelProviderFactory

    lateinit var viewModel: AccountViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangesListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement DataStateChangeListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(activity as AppCompatActivity, R.id.accountFragment)


        viewModel = activity?.run {
            ViewModelProvider(this, factory).get(AccountViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        cancelActiveJobs()
    }

    override fun cancelActiveJobs(){
        viewModel.cancelActiveJobs()
    }


}