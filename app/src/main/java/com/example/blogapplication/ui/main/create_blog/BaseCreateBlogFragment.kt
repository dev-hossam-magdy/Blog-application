package com.example.blogapplication.ui.main.create_blog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.blogapplication.ViewModelProviderFactory
import com.example.blogapplication.ui.DataStateChangesListener
import com.example.blogapplication.ui.RunTimePermissionListener
import com.example.blogapplication.ui.UICommunicationListener
import com.example.blogapplication.ui.main.BaseMainFragment
import java.lang.Exception
import javax.inject.Inject

abstract class BaseCreateBlogFragment : BaseMainFragment(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var factory: ViewModelProviderFactory
    @Inject
    lateinit var viewModel: CreateBlogViewModel
    @Inject
    protected lateinit var requestManager: RequestManager
    protected lateinit var uiCommunicationListener: UICommunicationListener


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            uiCommunicationListener = context as UICommunicationListener
        }catch (e:ClassCastException){
            Log.e(TAG, "$context must implement UICommunicationListener" )
        }



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProvider(this,factory).get(CreateBlogViewModel::class.java)
        }?:throw Exception("Activity not created")
    }

    override fun cancelActiveJobs(){
        viewModel.cancelActiveJobs()
    }
}