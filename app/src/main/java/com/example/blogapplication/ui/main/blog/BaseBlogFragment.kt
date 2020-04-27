package com.example.blogapplication.ui.main.blog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.example.blogapplication.R
import com.example.blogapplication.ViewModelProviderFactory
import com.example.blogapplication.ui.DataStateChangesListener
import com.example.blogapplication.ui.main.BaseMainFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseBlogFragment : BaseMainFragment(){

    val TAG: String = "BaseBlogFragment"

    @Inject
    lateinit var factory: ViewModelProviderFactory
    lateinit var viewModel:BlogPostViewModel

    lateinit var stateChangeListener: DataStateChangesListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangesListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(activity as AppCompatActivity , R.id.createBlogFragment)

        try {
            viewModel = activity?.run {

                ViewModelProvider(this,factory).get(BlogPostViewModel::class.java)
            }?:throw Exception("Invalid activity")
        }catch (e:Exception){
            Log.e(TAG,"there is an exaption is ${e.message}")
        }



    }

    override fun cancelActiveJobs(){
        viewModel.cancelActiveJobs()
    }



}