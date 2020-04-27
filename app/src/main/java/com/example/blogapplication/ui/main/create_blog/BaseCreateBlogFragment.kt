package com.example.blogapplication.ui.main.create_blog

import android.content.Context
import android.util.Log
import com.example.blogapplication.ui.DataStateChangesListener
import com.example.blogapplication.ui.main.BaseMainFragment
import dagger.android.support.DaggerFragment

abstract class BaseCreateBlogFragment : BaseMainFragment(){

    val TAG: String = "AppDebug"

    lateinit var stateChangeListener: DataStateChangesListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangesListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangesListener" )
        }
    }

    override fun cancelActiveJobs(){
       // viewModel.cancelActiveJobs()
    }
}